package com.yunseong.secod_project_web_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyInfoController {

    @GetMapping("/myinfo")
    public String getMyInfo() {
        return "/myinfo";
    }
}
