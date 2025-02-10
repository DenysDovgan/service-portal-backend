package com.colorlaboratory.serviceportalbackend;

import org.springframework.boot.SpringApplication;

public class TestServicePortalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(ServicePortalBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
