package com.mzrt.erp_lite.adapter.out.persistence.aws.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "aws.s3")
public record AwsModelConfig(
            @NotBlank(message = "aws.s3.endpoint no puede estar vacío")
            String endpoint,

            @NotBlank(message = "aws.s3.region no puede estar vacío")
            String region,

            @NotBlank(message = "aws.s3.access-key no puede estar vacío")
            String accessKey,

            @NotBlank(message = "aws.s3.secret-key no puede estar vacío")
            String secretKey,

            @NotBlank(message = "aws.s3.bucket-name no puede estar vacío")
            String bucketName,

            boolean pathStyleEnabled
    ) {

    public String getBucketUrl(){
        return String.format("%s/%s", endpoint,bucketName);
    }
}

