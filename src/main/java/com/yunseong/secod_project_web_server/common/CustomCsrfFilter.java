package com.yunseong.secod_project_web_server.common;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class CustomCsrfFilter extends GenericFilterBean {

    private CustomCookieCsrfTokenRepository customCookieCsrfTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(!httpServletRequest.getMethod().equalsIgnoreCase("get")) {
            CsrfToken csrfToken = this.customCookieCsrfTokenRepository.loadToken(httpServletRequest);
            String actualToken = httpServletRequest.getHeader(csrfToken.getHeaderName());
            if (actualToken == null) {
                actualToken = httpServletRequest.getParameter(csrfToken.getParameterName());
            }
            if(csrfToken.getToken().equals(actualToken)) {
                this.customCookieCsrfTokenRepository.refreshToken(httpServletRequest, (HttpServletResponse)response);
            }
        }
        chain.doFilter(request, response);
    }
}
