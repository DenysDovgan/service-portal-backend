package com.colorlaboratory.serviceportalbackend.service.gcs;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GcsService {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;


    // todo:: add issue id to file name
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName).build(),
                file.getInputStream()
        );
        return blobInfo.getMediaLink();
    }

    public String generateSignedUrl(String fileUrl, int durationInMinutes) {
        String fileName = extractFileNameFromUrl(fileUrl);

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, fileName)).build();

        URL signedUrl = storage.signUrl(
                blobInfo,
                durationInMinutes,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature()
        );

        log.info("Generated signed URL for {} valid for {} minutes", fileName, durationInMinutes);
        return signedUrl.toString();
    }


    public void deleteFile(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);

        BlobId blobId = BlobId.of(bucketName, fileName);
        boolean deleted = storage.delete(blobId);

        if (deleted) {
            log.info("File {} deleted successfully from Google Cloud Storage.", fileName);
        } else {
            log.warn("File {} not found in Google Cloud Storage.", fileName);
            throw new RuntimeException("File not found in Google Cloud Storage.");
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        // Remove query parameters (everything after '?')
        String cleanUrl = fileUrl.split("\\?")[0];

        // Extract the file name from the URL
        return cleanUrl.substring(cleanUrl.lastIndexOf('/') + 1);
    }

}
