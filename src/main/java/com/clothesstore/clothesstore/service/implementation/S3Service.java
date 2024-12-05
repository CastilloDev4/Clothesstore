package com.clothesstore.clothesstore.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;


;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;


    @Value("${aws.bucket.name}")
    private String bucketName;


}
