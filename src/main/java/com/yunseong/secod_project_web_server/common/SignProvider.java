package com.yunseong.secod_project_web_server.common;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Component
public class SignProvider {

    private final String HEADER = "X-AUTH-TOKEN";

    public void signIn(HttpHeaders headers, HttpServletRequest request) {
        String accessToken = headers.get(this.HEADER).get(0);
        HttpSession session = request.getSession();
        session.setAttribute(this.HEADER, accessToken);

        Cookie cookie = new Cookie(this.HEADER, accessToken);
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(false);
    }

    public void signOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(this.HEADER);

        Cookie cookie = new Cookie(this.HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
    }

    public boolean isLogin(HttpServletRequest request) {
        return !StringUtils.isEmpty(this.getSessionToken(request));
    }

    public Cookie getAccessTokenCookie(HttpServletRequest request) {
        if(request.getCookies() == null || request.getCookies().length == 0) return null;
        return Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase(this.HEADER))
                .findFirst().orElse(null);
    }

    public String getSessionToken(HttpServletRequest request) {
        Object object = request.getSession().getAttribute(this.HEADER);
        return (object != null) ? object.toString() : null;
    }
}
