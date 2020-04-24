package com.yunseong.secod_project_web_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.common.security.MyUser;
import com.yunseong.secod_project_web_server.controller.dto.MyInfoUpdateForm;
import com.yunseong.secod_project_web_server.controller.dto.ProductDto;
import com.yunseong.secod_project_web_server.controller.dto.ProductPage;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/myinfo")
public class MyInfoController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String getMyInfo(Model model) throws JsonProcessingException {
        ResponseEntity<String> entity = restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/members/me", String.class);

        Map<String, Object> map = objectMapper.readValue(entity.getBody(), HashMap.class);

        model.addAttribute("username", map.get("username"));
        model.addAttribute("nickname", map.get("nickname"));
        model.addAttribute("money", map.get("money"));
        model.addAttribute("grade", map.get("grade"));

        return "/myinfo";
    }

    @GetMapping("/purchase")
    public String getMyInfoPurchase(Model model) throws JsonProcessingException {
        ResponseEntity<String> entity = restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/members/me", String.class);

        Map<String, Object> map = objectMapper.readValue(entity.getBody(), HashMap.class);

        model.addAttribute("purchase", map.get("purchase"));

        return "/myinfopurchase";
    }

    @GetMapping("/update")
    public String getMyInfoUpdate(Model model) {
        model.addAttribute("myInfoForm", new MyInfoUpdateForm());
        return "/myinfoupdate";
    }

    @GetMapping("/carts")
    public String getCarts(Model model) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/carts", String.class);

        Map<String, Object> all = this.objectMapper.readValue(responseEntity.getBody(), HashMap.class);

        List list = (ArrayList) all.get("cartItems");
        if(list == null) return null;
        model.addAttribute("all", list);

        return "/myinfocart";
    }

    @PostMapping("/update")
    public String postMyInfoUpdate(MyInfoUpdateForm form) {
        HttpEntity entity = new HttpEntity(form);
        this.restTemplate.exchange(Util.REST_API_SERVER_URL + "/members/me", HttpMethod.PUT, entity, String.class);

        return "redirect:/myinfo";
    }
}
