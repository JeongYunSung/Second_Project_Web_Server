package com.yunseong.secod_project_web_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.common.security.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductsController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/products")
    public String getProducts(@AuthenticationPrincipal MyUser myUser, Model model) throws JsonProcessingException {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(Util.ACCESS_TOKEN, myUser.getAccessToken());
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products",  String.class);

        Map<String, Object> map = this.objectMapper.readValue(responseEntity.getBody(), HashMap.class);

        return "/products";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable Long id, @AuthenticationPrincipal MyUser myUser, Model model) throws JsonProcessingException {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(Util.ACCESS_TOKEN, myUser.getAccessToken());
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products/" + id, String.class);

        Map<String, Object> map = this.objectMapper.readValue(responseEntity.getBody(), HashMap.class);

        return "/productdetails";
    }
}
