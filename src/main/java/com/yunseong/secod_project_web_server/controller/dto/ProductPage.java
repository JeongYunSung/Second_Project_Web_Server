package com.yunseong.secod_project_web_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProductPage {

    private int number;
    private int startPage;
    private int endPage;
    private int maxPage;
}
