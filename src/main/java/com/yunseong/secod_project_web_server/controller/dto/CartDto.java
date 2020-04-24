package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartDto {

    private Long productId;
    private String title;
    private String description;
    private String value;
}
