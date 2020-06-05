package com.ramismayilov.batchone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class BatchoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchoneApplication.class, args);
    }

}
