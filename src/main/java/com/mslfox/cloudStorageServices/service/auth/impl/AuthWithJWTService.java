package com.mslfox.cloudStorageServices.service.auth.impl;

import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.mslfox.cloudStorageServices.dto.auth.AuthRequest;
import com.mslfox.cloudStorageServices.entities.auth.UserEntity;
import com.mslfox.cloudStorageServices.exception.BadRequestException;
import com.mslfox.cloudStorageServices.model.auth.TokenResponse;
import com.mslfox.cloudStorageServices.repository.auth.UserEntityRepository;
import com.mslfox.cloudStorageServices.security.JwtProvider;
import com.mslfox.cloudStorageServices.service.auth.AuthTokenService;
import com.mslfox.cloudStorageServices.util.Base64EncoderUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Data
@ConfigurationProperties("user")
public class AuthWithJWTService implements AuthTokenService<TokenResponse, AuthRequest> {
    private boolean autoCreation;
    private final UserEntityRepository userEntityRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(AuthRequest authRequest) throws RuntimeException {
        System.out.println(autoCreation);
        UserEntity userEntity;
        try {
            userEntity = loadUserByUsername(getUsernameEncoded(authRequest.getUsername()));
        } catch (UsernameNotFoundException e) {
            //TODO удалить авто-создание UserEntity -> boolean autoCreation, @ConfigurationProperties("user"), блок if()
            // оставить ->
            // throw BadRequestException.builder()
            //     .message(ConstantsHolder.ERROR_USER_NOT_FOUND).build();.
            if (autoCreation) {
                userEntity = new UserEntity(getUsernameEncoded(authRequest.getUsername()), passwordEncoder.encode(authRequest.getPassword()));
                System.out.println(userEntity);
                userEntityRepository.save(userEntity);
            } else {
                throw BadRequestException.builder()
                        .message(ConstantsHolder.ERROR_USER_NOT_FOUND).build();
            }
        }
        final var userEncodedPassword = userEntity.getPassword();
        final var authRequestPassword = authRequest.getPassword();
        if (passwordEncoder.matches(authRequestPassword, userEncodedPassword)) {
            final var jwt = jwtProvider.generateJwt(userEntity);
            return TokenResponse.builder().authToken(jwt).build();
        } else {
            throw BadRequestException.builder()
                    .message(ConstantsHolder.ERROR_WRONG_PASSWORD).build();
        }
    }

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ConstantsHolder.ERROR_USER_NOT_FOUND));
    }

    private String getUsernameEncoded(String originalString) {
        return Base64EncoderUtil.originalStringToBase64String(originalString);
    }

}
