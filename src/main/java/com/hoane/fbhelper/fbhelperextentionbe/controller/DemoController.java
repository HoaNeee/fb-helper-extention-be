package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.utils.MyRequestContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/ping")
    public String ping() {

        System.out.println(MyRequestContext.getRequestPlatform());
        System.out.println(MyRequestContext.getDeviceId());

        return "pong";
    }
}
