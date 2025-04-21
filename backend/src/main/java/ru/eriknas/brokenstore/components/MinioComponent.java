package ru.eriknas.brokenstore.components;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.eriknas.brokenstore.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
public class MinioComponent {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private boolean bucketExists;

    public void putObject(String objectName, InputStream inputStream) throws Exception {
        try {
            makeBucketIfNotExists(bucketName);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, -1, 10485760)
                            .build());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public String getObject(String objectName) throws Exception {
        try (InputStream stream = minioClient
                .getObject(GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build())) {
            return new String(stream.readAllBytes());
        } catch (ErrorResponseException notFoundException) {
            throw new NotFoundException(notFoundException.getMessage());
        } catch (Exception e) {
            throw new InternalException(e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    public void removeObject(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    private void makeBucketIfNotExists(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!bucketExists) {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build();
            bucketExists = minioClient.bucketExists(bucketExistsArgs);
            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }
            bucketExists = true;
        }
    }
}