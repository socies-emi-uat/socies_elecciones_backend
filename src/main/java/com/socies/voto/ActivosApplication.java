package com.socies.voto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ActivosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivosApplication.class, args);
    }
}
