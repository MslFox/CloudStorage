package com.mslfox.cloudStorageServices.controller.auth;

import com.mslfox.cloudStorageServices.dto.auth.AuthRequest;
import com.mslfox.cloudStorageServices.model.auth.TokenResponse;
import com.mslfox.cloudStorageServices.model.error.ErrorResponse;
import com.mslfox.cloudStorageServices.service.auth.impl.AuthWithJWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and logout")
public class AuthController {
    private AuthWithJWTService authWithJWTService;

    @Operation(summary = "Authenticate user", description = "Returns a token for a valid user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid credentials",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("login")
    public TokenResponse login(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        return authWithJWTService.login(authRequest);
    }

    @Operation(summary = "Logout user", description = "Invalidate JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful logout")})
    @PostMapping("logout")
    public void logout() {
        // Handled by LogoutHandlerWithJWTBlacklist
    }
}
