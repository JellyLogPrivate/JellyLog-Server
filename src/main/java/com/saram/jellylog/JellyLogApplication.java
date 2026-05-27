package com.saram.jellylog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
public class JellyLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(JellyLogApplication.class, args);
    }

}
