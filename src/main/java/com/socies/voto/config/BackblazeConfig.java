package com.socies.voto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "backblaze")
public class BackblazeConfig {

    private String keyId;
    private String applicationKey;
    private String bucketName;
    private String endpoint;

    // Constructores
    public BackblazeConfig() {}

    // Getters y Setters
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "BackblazeConfig{"
                + "keyId='"
                + keyId
                + '\''
                + ", applicationKey='[HIDDEN]'"
                + ", bucketName='"
                + bucketName
                + '\''
                + ", endpoint='"
                + endpoint
                + '\''
                + '}';
    }
}
