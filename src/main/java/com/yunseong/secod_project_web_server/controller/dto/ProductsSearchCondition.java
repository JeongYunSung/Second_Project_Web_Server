package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsSearchCondition {

    private String category;
    private String product;
    private Integer min;
    private Integer max;
}
