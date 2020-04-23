package com.yunseong.secod_project_web_server.controller;

import com.yunseong.secod_project_web_server.controller.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/carts")
public class CartController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public String postCart(@ModelAttribute ProductDto productDto) {
        System.out.println(productDto);
//        this.restTemplate.exchange(Util.REST_API_SERVER_URL + "/carts/itmes/new", HttpMethod.PUT, )

        return "redirect:/";
    }
}
