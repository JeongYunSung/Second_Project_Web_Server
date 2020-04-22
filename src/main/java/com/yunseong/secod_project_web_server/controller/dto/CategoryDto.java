package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CategoryDto {

    private final int id;
    private final String categoryName;
    private List<CategoryDto> childs;
}
