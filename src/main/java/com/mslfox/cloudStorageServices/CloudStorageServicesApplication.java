package com.mslfox.cloudStorageServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CloudStorageServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudStorageServicesApplication.class, args);
    }
}
