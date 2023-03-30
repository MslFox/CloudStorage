package com.mslfox.cloudStorageServices.model.auth;

import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenResponse {
    @JsonProperty(ConstantsHolder.TOKEN_HEADER_NAME)
    private final String authToken;
}


