package com.hoane.fbhelper.fbhelperextentionbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FbHelperExtentionBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FbHelperExtentionBeApplication.class, args);
    }

}
