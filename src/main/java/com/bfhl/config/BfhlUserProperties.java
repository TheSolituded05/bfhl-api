package com.bfhl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bfhl.user")
public class BfhlUserProperties {
    private String name;
    private String dob;
    private String email;
    private String rollNumber;
}
