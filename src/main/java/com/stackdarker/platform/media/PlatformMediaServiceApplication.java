package com.stackdarker.platform.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PlatformMediaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatformMediaServiceApplication.class, args);
    }
}
