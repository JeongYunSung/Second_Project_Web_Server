package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductForm {

    private String productName;
    private String description;
    private int value;
    private List<Integer> types;
}
