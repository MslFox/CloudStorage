package com.mslfox.cloudStorageServices.controller.auth;

import com.mslfox.cloudStorageServices.dto.auth.AuthRequest;
import com.mslfox.cloudStorageServices.model.auth.TokenResponse;
import com.mslfox.cloudStorageServices.service.auth.impl.AuthWithJWTService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthWithJWTService authWithJWTService;

    @PostMapping("login")
    public TokenResponse login(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        return authWithJWTService.login(authRequest);
    }

    @PostMapping("logout")
    public void logout() {
        // Handled by JwtBlacklistLogoutHandler
    }
}
