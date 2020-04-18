package com.yunseong.secod_project_web_server.common;

import com.yunseong.secod_project_web_server.common.exception.InvalidAccessTokenException;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@AllArgsConstructor
public class SignFilter extends GenericFilterBean {

    private SignProvider signProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Cookie cookie = this.signProvider.getAccessTokenCookie(httpServletRequest);
        String sessionToken = this.signProvider.getSessionToken(httpServletRequest);

        if(cookie != null && sessionToken != null && !sessionToken.equals(cookie.getValue())) {
            throw new InvalidAccessTokenException(sessionToken);
        }

        chain.doFilter(request, response);
    }
}
