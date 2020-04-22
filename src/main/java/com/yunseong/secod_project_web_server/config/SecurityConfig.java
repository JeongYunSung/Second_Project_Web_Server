package com.yunseong.secod_project_web_server.config;

import com.yunseong.secod_project_web_server.common.security.SignAuthenticationFilter;
import com.yunseong.secod_project_web_server.common.security.WebAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/css/**");
        web.ignoring().mvcMatchers("/js/**");
        web.ignoring().mvcMatchers("/images/**");
        web.ignoring().mvcMatchers("**/favicon.ico");
        web.ignoring().mvcMatchers("/fragments/**");
        web.ignoring().antMatchers("/test");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .sessionManagement()
                    .sessionAuthenticationStrategy(this.sessionAuthenticationStrategy)
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/", "/products", "/products/search").permitAll()
                        .antMatchers(HttpMethod.GET, "/signin", "/signup").anonymous()
                        .antMatchers(HttpMethod.POST, "/signin", "/signup").anonymous()
                        .anyRequest().authenticated()
                .and()
                    .addFilterBefore(new SignAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    public static String getClientIp(HttpServletRequest request) {
//        String ip = request.getHeader("X-Forwarded-For");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }
}
