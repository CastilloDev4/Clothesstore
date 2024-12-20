package com.clothesstore.clothesstore.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {

        Region region = Region.US_EAST_2;
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
        return s3Client;
    }
}
