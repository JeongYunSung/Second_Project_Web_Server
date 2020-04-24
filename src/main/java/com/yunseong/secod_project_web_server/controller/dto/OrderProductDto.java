package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDto {

    private int productId;
    private String productName;
    private int productPrice;
}
