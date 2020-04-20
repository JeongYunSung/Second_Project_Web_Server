package com.yunseong.secod_project_web_server.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.*;

@RequiredArgsConstructor
public class CustomHttpSessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
//        if (se.getName().equals(SignProvider.USER_HEADER)) {
//            HttpSession session = se.getSession();
//            this.customRegisterSessionAuthenticationStrategy.sessionExpireNow(session.getAttribute(SignProvider.USER_HEADER));
//        }
    }
}
