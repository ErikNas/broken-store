package ru.eriknas.brokenstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;
    @Value("${minio.bucket-name}")
    private String bucketName;
    @Value("${minio.secure}")
    private Boolean minioSecure;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(minioUrl, 9000, minioSecure)
                .build();
    }
}