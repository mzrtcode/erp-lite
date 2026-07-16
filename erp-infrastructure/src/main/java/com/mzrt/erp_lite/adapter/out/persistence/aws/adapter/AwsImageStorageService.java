package com.mzrt.erp_lite.adapter.out.persistence.aws.adapter;

import com.mzrt.erp_lite.adapter.out.persistence.aws.model.AwsModelConfig;
import com.mzrt.erp_lite.domain.exception.MyBusinessException;
import com.mzrt.erp_lite.domain.product.ProductImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.mzrt.erp_lite.edomain.ports.ImageStorageService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsImageStorageService implements ImageStorageService{

    private final S3Client s3Client;
    private final AwsModelConfig awsConfig;


    @Override
    public ProductImage upload(String name, byte[] imageData) {
        try{
            final var key = "products/" + name;
            final var putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsConfig.bucketName())
                    .key(key)
                    .contentType(determineContentType(name))
                    .contentLength((long)imageData.length)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));
            final var imageUrl = this.buildUrlImg(key);
            log.info("Image uploaded successfully");

            return new ProductImage(imageUrl);

        }catch (S3Exception s3e){
            log.error("Error uploading image", s3e);
            throw new MyBusinessException("Error uploading image " + s3e.getMessage());
        }catch (Exception e){
            log.error("Unexpected error uploading image", e);
            throw new MyBusinessException("Error uploading image " + e.getMessage());

        }
    }

    @Override
    public void delete(ProductImage img) {
        try{
            final var key = this.getKeyFromUrl(img.imageUrl());
            final var deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(awsConfig.bucketName())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Image successfully deleted");

        }catch (S3Exception s3e){
            log.error("Error deleting image", s3e);
            throw new MyBusinessException("Error deleting image " + s3e.getMessage());
        }catch (Exception e){
            log.error("Unexpected error deleting image", e);
            throw new MyBusinessException("Error deleting image " + e.getMessage());

        }
    }

    @Override
    public byte[] download(ProductImage img) {
        try{

            final var key = this.getKeyFromUrl(img.imageUrl());
            final var getObjectRequest = GetObjectRequest.builder()
                    .bucket(awsConfig.bucketName())
                    .key(key)
                    .build();

            final var bytes = s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
            log.info("Downloading image: {} bytes", bytes.length);

            return bytes;

        }catch (S3Exception s3e){
            log.error("Error downloading image", s3e);
            throw new MyBusinessException("Error downloading image " + s3e.getMessage());
        }catch (Exception e){
            log.error("Unexpected error downloading image", e);
            throw new MyBusinessException("Error downloading image " + e.getMessage());

        }
    }


    /**
     *
     * @param url https://amazonaws/erp-products/products/mac-01.png
     * @return /products/mac-01.png
     */
    private String getKeyFromUrl(String url){
        var bucketName = awsConfig.bucketName();
        var parts = url.split("/"  + bucketName + "/");

        if (parts.length > 1 ){
            return parts[1];
        }
        log.warn("No bucket name found from url: {}", url);
        return url;
    }

    private String buildUrlImg(String key){
        final var placeHolder = "%s/%s/%s";
        return String.format(placeHolder,
                awsConfig.endpoint(),
                awsConfig.bucketName(),
                key);
    }

    private String determineContentType(String fileName){
        final var extension = fileName.substring(fileName.lastIndexOf("."), + 1).toLowerCase();
        return switch (extension){
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            default -> "application/octect-stream";
        };
    }

}
