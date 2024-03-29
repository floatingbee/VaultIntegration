package com.soc.springboothashicorpvault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.core.VaultTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Configuration
@ConfigurationProperties
@EnableScheduling
public class MyConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MyConfiguration.class);

    private String username;
    private String password;

    private final VaultTemplate vaultTemplate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Autowired
    public MyConfiguration(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

}
