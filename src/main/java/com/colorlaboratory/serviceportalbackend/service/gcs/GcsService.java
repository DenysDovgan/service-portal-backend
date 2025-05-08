package com.colorlaboratory.serviceportalbackend.service.gcs;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GcsService {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;


    public InputStream downloadFile(String fileUrl) throws FileNotFoundException {
        String objectName = extractFileNameFromUrl(fileUrl);
        log.info("Downloading file from GCS: {}", objectName);
        Blob blob = storage.get(bucketName, objectName);

        if (blob == null || !blob.exists()) {
            throw new FileNotFoundException("File not found in GCS");
        }

        return new ByteArrayInputStream(blob.getContent());
    }

    public String uploadFile(MultipartFile file, Long issueId) throws IOException {
        String fileName = UUID.randomUUID() + "_" + issueId;
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName).build(),
                file.getInputStream()
        );
        return blobInfo.getMediaLink();
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

    public static String extractFileNameFromUrl(String fileUrl) {
        // Remove query parameters (everything after '?')
        String cleanUrl = fileUrl.split("\\?")[0];

        // Extract the file name from the URL
        return cleanUrl.substring(cleanUrl.lastIndexOf('/') + 1);
    }

}
