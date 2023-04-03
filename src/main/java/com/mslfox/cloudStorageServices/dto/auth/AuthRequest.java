package com.mslfox.cloudStorageServices.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mslfox.cloudStorageServices.constant.HeaderNameHolder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@Schema(name = "credentials", description = "Contains non-empty login validated by the @Email constraint")
public class AuthRequest {
    @NotEmpty
    @Email
    @Size(min = 6, max = 64)
    @JsonProperty(HeaderNameHolder.USER_HEADER_NAME)
    private String username;
    @NotEmpty
    @Size(min = 6, max = 64)
    private String password;
}

