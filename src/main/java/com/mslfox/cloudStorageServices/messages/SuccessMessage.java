package com.mslfox.cloudStorageServices.messages;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Setter
@ConfigurationProperties("success")
public class SuccessMessage {
    public String delete;
    public String rename;
    public String upload;
}
