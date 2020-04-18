package com.yunseong.secod_project_web_server.config;

import com.yunseong.secod_project_web_server.common.CustomCookieCsrfTokenRepository;
import com.yunseong.secod_project_web_server.common.CustomCsrfFilter;
import com.yunseong.secod_project_web_server.common.SignFilter;
import com.yunseong.secod_project_web_server.common.SignProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomCookieCsrfTokenRepository customCookieCsrfTokenRepository;
    @Autowired
    private SignProvider signProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(PathRequest.toStaticResources().atCommonLocations().toString());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().csrfTokenRepository(this.customCookieCsrfTokenRepository)
                .and()
                .httpBasic().disable()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .addFilterAfter(new CustomCsrfFilter(this.customCookieCsrfTokenRepository), CsrfFilter.class)
                .addFilterAfter(new SignFilter(this.signProvider), CustomCsrfFilter.class);
    }
}
