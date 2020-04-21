package com.yunseong.secod_project_web_server.common.security;

import com.yunseong.secod_project_web_server.common.Util;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String path = request.getURI().getPath();
        if(!(path.equals("signin") || path.equals("signup"))) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if(principal instanceof MyUser) {
                    MyUser myUser = (MyUser) principal;
                    request.getHeaders().set(Util.ACCESS_TOKEN, myUser.getAccessToken());
                }
            }
        }
        return execution.execute(request, body);
    }
}
