package com.mslfox.cloudStorageServices.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class AuthRequest {
    @NotEmpty
    @Email
    @Size(min = 6, max = 64)
    @JsonProperty(ConstantsHolder.USER_HEADER_NAME)
    private String username;
    @NotEmpty
    private String password;
}

