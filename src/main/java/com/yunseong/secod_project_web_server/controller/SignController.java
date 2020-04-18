package com.yunseong.secod_project_web_server.controller;

import com.yunseong.secod_project_web_server.common.SignProvider;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.controller.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SignProvider signProvider;

    @GetMapping("/signin")
    public String getSignIn() {
        return "/signIn.html";
    }

    @GetMapping("/signup")
    public String getSignUp() {
        return "/signUp.html";
    }

    @PostMapping("/signin")
    public String postSignIn(HttpServletRequest request, HttpServletResponse response, User user) {
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/v1/members/siginin", entity, String.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            return "redirect:/signin";
        } else if (statusCode.is4xxClientError()) {
        }
        return "redirect:/";
    }

    @PostMapping("/signout")
    public String postSignOut(HttpServletRequest request) {
        if (!this.signProvider.isLogin(request)) {
            return "redirect:/signin";
        }
        this.signProvider.signOut(request);

        return "redirect:/";
    }
}
