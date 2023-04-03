package com.mslfox.cloudStorageServices.messages;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter
@ConfigurationProperties("error")
public class ErrorMessage {
    public String wrongPassword;
    public String userNotFound;
    public String gettingFile;
    public String gettingFileList;
    public String renameFile;
    public String uploadFile;
    public String deleteFile;
    public String internal;
    public String validation;
    public String securityInvalidUsername;
    public String jsonProcessing;
}
