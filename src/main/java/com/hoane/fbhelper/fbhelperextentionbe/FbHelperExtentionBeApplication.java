package com.hoane.fbhelper.fbhelperextentionbe;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FbHelperExtentionBeApplication {

    public static void main(String[] args) {
        // Look for .env relative to where the JAR/class is, fallback to working dir
        String projectDir = System.getProperty("user.dir");
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

//        System.out.println(">>> Dotenv loaded entries: " + dotenv.entries().size());

        dotenv.entries().forEach(entry -> {
//            System.out.println(">>> " + entry.getKey() + "=" + entry.getValue());
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(FbHelperExtentionBeApplication.class, args);
    }

}
