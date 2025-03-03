package com.example.top.pages.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class S3Storage {
    String accessKey = "daa783aeb3f34b0ea139d16a7779f759";
    String secretKey = "85bff1ad02794b918f616d34c44afc60";
    String endpoint = "https://s3.ru-1.storage.selcloud.ru";
    String bucketName = "s3toppages";
    AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    String regionName = "ru-1";

    private S3Client getClient() {
        return S3Client
                .builder()
                .region(Region.of(regionName))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public void uploadFileToS3(String nameFile, MultipartFile multipartFile) {
        S3Client s3Client = getClient();
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(nameFile)
                            .build(),
                    RequestBody.fromBytes(multipartFile.getBytes()));
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] downloadFileFromS3(String nameFile) {
        S3Client s3Client = getClient();
        String key = "baeldung-logo.png";
        Path downloadPath = Paths.get("test1.jpg");

        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(nameFile)
                .bucket(bucketName)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        return objectBytes.asByteArray();

//        s3Client.getObjectAsBytes(request ->
//                        request
//                                .bucket(bucketName)
//                                .key(nameFile),
//                ResponseTransformer.toFile(downloadPath));

    }

}
