package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeoIpService {
    private final DatabaseReader databaseReader;

    public GeoIpService(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public Map<String, String> getLocation(String ipAddress) {
        Map<String, String> locationMap = new HashMap<>();
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);

            CityResponse response = databaseReader.city(ip);

            String country = response.country().names().get("en");
            String city = response.city().names().get("en");

            locationMap.put("country", country == null ? "Unknown" : country);
            locationMap.put("city", city == null ? "Unknown" : city);

        } catch (Exception e) {
            locationMap.put("error", "Không thể tìm thấy vị trí cho IP này: " + e.getMessage());
        }
        return locationMap;
    }
}
