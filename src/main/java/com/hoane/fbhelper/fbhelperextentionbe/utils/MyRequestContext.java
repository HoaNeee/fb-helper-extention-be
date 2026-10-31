package com.hoane.fbhelper.fbhelperextentionbe.utils;


import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.RequestPlatform;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public final class MyRequestContext {
    private MyRequestContext() {
    }

    public static RequestPlatform getRequestPlatform() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes == null) return Utils.getRequestPlatform("DEVICE");

        String platform = (String) attributes.getAttribute("requestPlatform", RequestAttributes.SCOPE_REQUEST);

        return Utils.getRequestPlatform(platform);
    }


    public static String getDeviceId() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes == null) return null;

        return (String) attributes.getAttribute("deviceId", RequestAttributes.SCOPE_REQUEST);
    }
}
