package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderForm {

    private List<Long> orderItems;
}
