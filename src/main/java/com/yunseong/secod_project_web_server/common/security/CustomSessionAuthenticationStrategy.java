package com.yunseong.secod_project_web_server.common.security;

import com.yunseong.secod_project_web_server.common.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@RequiredArgsConstructor
public class CustomSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private final Map<String, HttpSession> cash = Collections.synchronizedMap(new WeakHashMap<>());

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.putSession(request.getSession().getId(), request.getSession());
    }

    public boolean createSession(String userId, String accessToken, HttpServletRequest request) {
        HttpSession session = request.getSession();
        MyUser value = new MyUser(userId, accessToken);
        session.setAttribute(Util.USER_HEADER, value);
        for(HttpSession httpSession : this.cash.values()) {
            if (httpSession.getAttribute(Util.USER_HEADER).equals(value)) {
                this.cash.remove(httpSession.getId());
                httpSession.invalidate();
                return true;
            }
        }
        this.putSession(session.getId(), session);
        return false;
    }

    public void removeSession(String sessionId) {
        if(this.cash.containsKey(sessionId)) {
            this.cash.get(sessionId).invalidate();
            this.cash.remove(sessionId);
        }
    }

    private void putSession(String id, HttpSession session) {
        if (!this.cash.containsKey(id)) {
            this.cash.put(id, session);
        }
    }
}
