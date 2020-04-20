package com.yunseong.secod_project_web_server.common.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "userId")
public class MyUser {

    private String userId;
    private String accessToken;

    public MyUser(String userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }
}
