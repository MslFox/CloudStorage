package com.mslfox.cloudStorageServices.service.authentication;

import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.mslfox.cloudStorageServices.dto.authentication.AuthRequest;
import com.mslfox.cloudStorageServices.entities.authentication.UserEntity;
import com.mslfox.cloudStorageServices.exception.BadRequestException;
import com.mslfox.cloudStorageServices.exception.DetectedException;
import com.mslfox.cloudStorageServices.model.authentication.AuthTokenResponse;
import com.mslfox.cloudStorageServices.repository.authentication.UserEntityRepository;
import com.mslfox.cloudStorageServices.security.JwtProvider;
import com.mslfox.cloudStorageServices.util.StringEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${user.auto.creation:false}")
    private boolean autoCreation;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;



    // TODO удалить авто-создание UserEntity. boolean autoCreation
    public AuthTokenResponse login(AuthRequest authRequest) throws DetectedException {
        UserEntity userEntity;
        try {
            userEntity = userEntityRepository.loadUserByUsername(getUsernameEncoded(authRequest.getUsername()));
        } catch (UsernameNotFoundException e) {
            if (autoCreation) {
                userEntity = new UserEntity(getUsernameEncoded(authRequest.getUsername()), passwordEncoder.encode(authRequest.getPassword()));
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
            return AuthTokenResponse.builder().authToken(jwt).build();
        } else {
            throw BadRequestException.builder()
                    .message(ConstantsHolder.ERROR_WRONG_PASSWORD).build();
        }
    }
    private String getUsernameEncoded(String originalString) {
        return StringEncoder.originalStringToBase64String(originalString);
    }
}
