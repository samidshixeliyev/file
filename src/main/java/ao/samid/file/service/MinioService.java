package ao.samid.file.service;


import ao.samid.file.config.MinioProperty;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperty minioConfigProperties;

    public String uploadFile(MultipartFile file , String uniqueFileName) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfigProperties.getBucket()).build());
            if (!found) {
                minioClient
                        .makeBucket(MakeBucketArgs.builder()
                                .bucket(minioConfigProperties.getBucket())
                                .build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioConfigProperties.getBucket()).object(uniqueFileName).stream(
                                    file.getInputStream(), file.getInputStream().available(), -1)
                            .contentType(file.getContentType())
                            .build());

            return getFileUrl(uniqueFileName);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    public byte[] downloadFile(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfigProperties.getBucket())
                            .object(fileName)
                            .build()).readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from MinIO: " + e.getMessage());
        }
    }

    public String getFileUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioConfigProperties.getBucket())
                            .object(fileName)
                            .expiry(60 * 60)
                            .method(Method.GET)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Error generating presigned URL: " + e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfigProperties.getBucket()).object(fileName).build());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from MinIO: " + e.getMessage());
        }
    }
}