package com.yunseong.secod_project_web_server.controller;

import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.controller.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/carts")
public class CartController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public String postCart(@ModelAttribute CartDto cartDto) {
        this.restTemplate.exchange(Util.REST_API_SERVER_URL + "/carts/items/new", HttpMethod.PUT, new HttpEntity<>(cartDto), String.class);
        return "redirect:/myinfo/carts";
    }

    @PostMapping("/{id}")
    public String deleteCart(@PathVariable Long id) {
        this.restTemplate.exchange(Util.REST_API_SERVER_URL + "/carts/items/{id}", HttpMethod.PUT, null, String.class, id);
        return "redirect:/myinfo/carts";
    }
}
