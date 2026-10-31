package com.hoane.fbhelper.fbhelperextentionbe.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestUtils {
    private static String IP_DEFAULT;

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "X-Real-IP"
    };


    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = null;
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && !ipList.isEmpty() && !"unknown".equalsIgnoreCase(ipList)) {
                // X-Forwarded-For có thể chứa chuỗi IP dạng: "Client-IP, Proxy1-IP, Proxy2-IP"
                // Ta chỉ lấy IP đầu tiên (IP gốc của Client)
                ip = ipList.split(",")[0].trim();
            }
        }

        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return convertIpLocal(ip);
        }

        ip = request.getRemoteAddr();

        return convertIpLocal(ip);
    }

    private static String convertIpLocal(String ip) {
        if (ip.equals("127.0.0.1") || ip.equals("localhost") || ip.equals("0:0:0:0:0:0:0:1")) {
            return RequestUtils.IP_DEFAULT;
        }
        return ip;
    }

    @Value("${app.ip-default}")
    public void setIpDefault(String ipDefault) {
        RequestUtils.IP_DEFAULT = ipDefault;
    }


    public static String getDeviceId(HttpServletRequest request) {
        return request.getHeader("X-Device-Id");
    }

}
