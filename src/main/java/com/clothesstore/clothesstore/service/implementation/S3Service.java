package com.clothesstore.clothesstore.service.implementation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.stereotype.Service;

import java.net.URL;


@Service
public class S3Service {
    private  AmazonS3 amazonS3;


    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }
    public boolean isImageSizeValid(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        String key = url.getPath().replaceFirst("/", "");

        ObjectMetadata metadata = amazonS3.getObjectMetadata(bucketName, key);
        long sizeInBytes = metadata.getContentLength();
        return sizeInBytes <= 1_048_576; //1mb

}
}