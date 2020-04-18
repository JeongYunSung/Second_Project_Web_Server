package com.yunseong.secod_project_web_server.common.exception;

import java.nio.file.AccessDeniedException;

public class InvalidAccessTokenException extends AccessDeniedException {

    public InvalidAccessTokenException(String accessToken) {
        super("Invalid Access Token");
    }
}
