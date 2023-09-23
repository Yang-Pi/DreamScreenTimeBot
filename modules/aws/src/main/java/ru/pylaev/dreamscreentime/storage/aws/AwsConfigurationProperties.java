package ru.pylaev.dreamscreentime.storage.aws;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws")
public record AwsConfigurationProperties(
        String accessKeyId,
        String secretAccessKey,
        String bucketName
){
}