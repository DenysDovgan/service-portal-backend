package com.colorlaboratory.serviceportalbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "media")
@Getter @Setter
public class MediaProperties {
    private Map<String, Long> maxSize;
    private List<String> allowedMimeTypes;
}
