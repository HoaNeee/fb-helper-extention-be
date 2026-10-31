package com.hoane.fbhelper.fbhelperextentionbe.utils;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.RequestPlatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static List<String> splitStringToList(String str) {
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

    }

    public static List<String> splitStringToList(String str, String key) {
        return Arrays.stream(str.split(key))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public static RequestPlatform getRequestPlatform(String platform) {
        try {
            return RequestPlatform.valueOf(platform);
        } catch (Exception e) {
            return RequestPlatform.DEVICE;
        }
    }
}
