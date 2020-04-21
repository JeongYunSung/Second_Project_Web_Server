package com.yunseong.secod_project_web_server.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyInfoUpdateForm {

    private String password;
    private String verifyPassword;
    private String nickname;
    private int money;
}
