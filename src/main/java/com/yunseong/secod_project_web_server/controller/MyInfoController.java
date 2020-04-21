package com.yunseong.secod_project_web_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.common.security.MyUser;
import com.yunseong.secod_project_web_server.controller.dto.MyInfoUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/myinfo")
public class MyInfoController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String getMyInfo(@AuthenticationPrincipal MyUser myUser, Model model) throws JsonProcessingException {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(Util.ACCESS_TOKEN, myUser.getAccessToken());
        ResponseEntity<String> entity = restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/members/me", String.class);

        Map<String, Object> map = objectMapper.readValue(entity.getBody(), HashMap.class);

        model.addAttribute("username", map.get("username"));
        model.addAttribute("nickname", map.get("nickname"));
        model.addAttribute("money", map.get("money"));
        model.addAttribute("grade", map.get("grade"));

        return "/myinfo";
    }

    @GetMapping("/update")
    public String getMyInfoUpdate(Model model) {
        model.addAttribute("myInfoForm", new MyInfoUpdateForm());
        return "/myinfoupdate";
    }

    @PostMapping("/update")
    public String postMyInfoUpdate(@AuthenticationPrincipal MyUser myUser, MyInfoUpdateForm form) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(Util.ACCESS_TOKEN, myUser.getAccessToken());
        HttpEntity entity = new HttpEntity(form);
        this.restTemplate.exchange(Util.REST_API_SERVER_URL + "/members/me", HttpMethod.PUT, entity, String.class);

        return "redirect:/myinfo";
    }
}
