package com.hoane.fbhelper.fbhelperextentionbe.utils;

import java.security.SecureRandom;

public class IdGenerator {
    // Tập ký tự dùng để tạo ID (Bạn có thể bỏ bớt ký tự nếu muốn)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateId(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Độ dài ID phải lớn hơn 0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
