package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {

    private int orderId;
    private List<OrderProductDto> products;
    private int totalPrice;
    private String orderStatus;
    private String paymentStatus;
}
