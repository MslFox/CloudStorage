package com.mslfox.cloudStorageServices.controller.authentication;

import com.mslfox.cloudStorageServices.dto.authentication.AuthRequest;
import com.mslfox.cloudStorageServices.model.authentication.AuthTokenResponse;
import com.mslfox.cloudStorageServices.service.authentication.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {

    private  AuthService authService;

    @PostMapping("login")
    public AuthTokenResponse login(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        return authService.login(authRequest);
    }

    @PostMapping("logout")
    public void logout() {
        // Handled by JwtBlacklistLogoutHandler
    }
}
