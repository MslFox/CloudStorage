package com.mslfox.cloudStorageServices.functionalTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mslfox.cloudStorageServices.constant.HeaderNameHolder;
import com.mslfox.cloudStorageServices.containers.TestPostgreSQLContainer;
import com.mslfox.cloudStorageServices.dto.auth.AuthRequest;
import com.mslfox.cloudStorageServices.entities.auth.UserEntity;
import com.mslfox.cloudStorageServices.messages.ErrorMessage;
import com.mslfox.cloudStorageServices.model.auth.TokenResponse;
import com.mslfox.cloudStorageServices.model.error.ErrorResponse;
import com.mslfox.cloudStorageServices.repository.auth.UserEntityRepository;
import com.mslfox.cloudStorageServices.repository.jwt.BlackJwtRepository;
import com.mslfox.cloudStorageServices.security.JwtProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Date;

import static com.mslfox.cloudStorageServices.constant.TestConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthModuleTest {
    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = TestPostgreSQLContainer.getInstance();

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private BlackJwtRepository blackJwtRepository;

    @Autowired
    private ErrorMessage errorMessage;
    @Autowired
    private MockMvc mockMvc;
    @Value("${security.jwt.secret}")
    private String secret;
    @Value("${security.jwt.authorities-claim-name}")
    private String authoritiesClaimName;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserEntity userEntity = new UserEntity(TEST_USERNAME_ENCODED, TEST_PASSWORD_ENCODED);

    @BeforeEach
    public void setup() {
        userEntityRepository.save(userEntity);
    }

    @AfterEach
    public void tearDown() {
        userEntityRepository.deleteAll();
    }

    @AfterAll
    public static void stopContainer() {
        POSTGRESQL_CONTAINER.stop();
        POSTGRESQL_CONTAINER.close();
    }

    @Test
    public void loginSuccess() throws Exception {
        final AuthRequest authRequest = new AuthRequest(TEST_USERNAME, TEST_PASSWORD);
        final var resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        resultActions.andExpect(status().isOk());
        assertDoesNotThrow(() -> objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TokenResponse.class));
        final var token = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TokenResponse.class);
        assertNotNull(token);
    }

    @Test
    public void loginWithUserNotFound() throws Exception {
        // Arrange
        final var authRequest = new AuthRequest(TEST_USERNAME_NON_EXISTENT, TEST_PASSWORD);
        // Act
        final var resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        // Assert
        resultActions.andExpect(status().isBadRequest());
        assertDoesNotThrow(() -> objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ErrorResponse.class));
        final var errorResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getMessage(), errorMessage.userNotFound());
    }

    @Test
    public void loginWithWrongPassword() throws Exception {
        // Arrange
        final AuthRequest authRequest = new AuthRequest(TEST_USERNAME, TEST_PASSWORD_WRONG);
        // Act
        final var resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        // Assert
        resultActions.andExpect(status().isBadRequest());
        assertDoesNotThrow(() -> objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ErrorResponse.class));
        final var errorResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(errorMessage.wrongPassword(), errorResponse.getMessage());
    }

    @Test
    @DisplayName("should save jwt in BlackJwtRepository")
    public void logoutWithValidJWT() throws Exception {
        blackJwtRepository.deleteAll();
        final var validJwtWithoutBearer = jwtProvider.generateJwt(userEntity);
        // Act
        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HeaderNameHolder.TOKEN_HEADER_NAME, "Bearer " + validJwtWithoutBearer));
        assertEquals(validJwtWithoutBearer, blackJwtRepository.findAll().get(0).getToken());
    }

    @Test
    @DisplayName("shouldn't save jwt in BlackJwtRepository")
    public void logoutWithInvalidJWT() throws Exception {
        blackJwtRepository.deleteAll();
        final var validJwtWithoutBearer = jwtProvider.generateJwt(userEntity);
        final var invalidJwtWithBearer = "Bearer " + validJwtWithoutBearer + 1;
        final var validExpiredToken = "Bearer " + Jwts.builder()
                .setSubject(userEntity.getUsername())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS256, secret)
                .claim(authoritiesClaimName, userEntity.getAuthorities())
                .compact();
        // Act auth-token not start with 'Bearer '
        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HeaderNameHolder.TOKEN_HEADER_NAME, validJwtWithoutBearer));
        // Act auth-token start with 'Bearer ', but not right jwt's format
        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HeaderNameHolder.TOKEN_HEADER_NAME, invalidJwtWithBearer));
        // Act auth-token is right format , but expired
        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HeaderNameHolder.TOKEN_HEADER_NAME, validExpiredToken));
        assert (blackJwtRepository.findAll().size() == 0);
    }
}
