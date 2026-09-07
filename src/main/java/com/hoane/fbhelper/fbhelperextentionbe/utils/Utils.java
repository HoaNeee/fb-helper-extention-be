package com.hoane.fbhelper.fbhelperextentionbe.utils;

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
}
