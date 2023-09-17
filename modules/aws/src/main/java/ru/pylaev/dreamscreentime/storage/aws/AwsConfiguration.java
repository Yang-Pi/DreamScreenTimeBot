package ru.pylaev.dreamscreentime.storage.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@AutoConfiguration
@EnableConfigurationProperties(AwsConfigurationProperties.class)
public class AwsConfiguration {

    @Bean
    @Profile("aws")
    AmazonS3Client amazonS3Client(AwsConfigurationProperties configurationProperties) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                configurationProperties.accessKeyId(), configurationProperties.secretAccessKey());
        return new AmazonS3Client(awsCredentials);
    }

    @Bean
    @Profile("aws")
    S3Images s3ImagesStorage(AmazonS3Client awsClient, AwsConfigurationProperties configurationProperties) {
        return new S3Images(awsClient, configurationProperties);
    }

}
