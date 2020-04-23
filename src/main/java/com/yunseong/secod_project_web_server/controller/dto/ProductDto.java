package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ProductDto {

    private int productId;
    private String productName;
    private String description;
    private int value;
    private int view;
    private int recommends;
    private String category;

    public ProductDto(Map<String, Object> map) {
        this.productId = (int)map.get("productId");
        this.productName = (String)map.get("productName");
        this.description = (String)map.get("description");
        this.value = (int)map.get("value");
        this.view = (int)map.get("view");
        Object recommends = map.get("recommends");
        if (recommends != null) {
            this.recommends = ((List)recommends).size();
        }
        this.category = (String)((List<LinkedHashMap>)map.get("types")).get(0).get("categoryName");
    }
}
