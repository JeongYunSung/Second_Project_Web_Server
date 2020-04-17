package com.yunseong.secod_project_web_server.controller;

import com.yunseong.secod_project_web_server.controller.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignInController {

    @GetMapping("/signin")
    public String getSignIn() {
        return "/signIn.html";
    }

    @PostMapping("/signin")
    public String postSignIn(User user) {
        System.out.println(user.getUsername() + " : " + user.getPassword());
        return "/index.html";
    }
}
