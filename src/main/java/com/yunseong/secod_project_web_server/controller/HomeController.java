package com.yunseong.secod_project_web_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.controller.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String home(Model model) throws JsonProcessingException {
        ResponseEntity<String> responseBest = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products/best", String.class);
        ResponseEntity<String> responseView = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products/view", String.class);
        ResponseEntity<String> responseRecent = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products?size={size}", String.class, 20);

        Map<String, Object> best = this.objectMapper.readValue(responseBest.getBody(), HashMap.class);
        Map<String, Object> view = this.objectMapper.readValue(responseView.getBody(), HashMap.class);
        Map<String, Object> all = this.objectMapper.readValue(responseRecent.getBody(), HashMap.class);

        model.addAttribute("best", convertMapToList(best));
        model.addAttribute("view", convertMapToList(view));
        model.addAttribute("all", convertMapToList(all));

        return "/index";
    }

    public List<ProductDto> convertMapToList(Map<String, Object> map) {
        Map detailMap = (LinkedHashMap) map.get("_embedded");
        if(detailMap == null) return null;
        List<Map> list = (ArrayList) detailMap.get("productResponseList");

        return list.stream().map(ProductDto::new).collect(Collectors.toList());
    }
}
