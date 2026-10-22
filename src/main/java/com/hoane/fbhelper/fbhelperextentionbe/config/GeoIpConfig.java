package com.hoane.fbhelper.fbhelperextentionbe.config;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GeoIpConfig {

    private final ResourceLoader resourceLoader;

    public GeoIpConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public DatabaseReader databaseReader() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:GeoLite2-City.mmdb");

        try (InputStream inputStream = resource.getInputStream()) {
            return new DatabaseReader.Builder(inputStream)
                    .withCache(new CHMCache())
                    .build();
        }
    }
}
