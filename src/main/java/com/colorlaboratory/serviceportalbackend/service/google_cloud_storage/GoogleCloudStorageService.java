package com.colorlaboratory.serviceportalbackend.service.google_cloud_storage;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCloudStorageService {

    private String bucketName;


    public String uploadFile(MultipartFile file) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
    }

}
