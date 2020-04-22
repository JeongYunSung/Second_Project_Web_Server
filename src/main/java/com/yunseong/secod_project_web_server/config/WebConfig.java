package com.yunseong.secod_project_web_server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.security.CustomClientHttpRequestInterceptor;
import com.yunseong.secod_project_web_server.common.security.CustomHttpSessionListener;
import com.yunseong.secod_project_web_server.common.security.CustomSessionAuthenticationStrategy;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSessionListener;
import java.util.Arrays;

@Configuration
public class WebConfig {

    @Value("${spring.restTemplate.factory.readTimeout}")
    private int READ_TIMEOUT;

    @Value("${spring.restTemplate.factory.connectTimeout}")
    private int CONNECT_TIMEOUT;

    @Value("${spring.restTemplate.httpClient.maxConnectionPool}")
    private int CONNECTION_POOL;

    @Value("${spring.restTemplate.httpClient.maxConnPerRoute}")
    private int CONN_PER_ROUTE;

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(this.CONNECT_TIMEOUT);
        factory.setReadTimeout(this.READ_TIMEOUT);

        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(this.CONNECTION_POOL)
                .setMaxConnPerRoute(this.CONN_PER_ROUTE).build();

        factory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Arrays.asList(new CustomClientHttpRequestInterceptor()));

        return restTemplate;
    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new CustomHttpSessionListener();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public CsrfAuthenticationStrategy csrfAuthenticationStrategy() {
        return new CsrfAuthenticationStrategy(csrfTokenRepository());
    }

    @Bean
    public CustomSessionAuthenticationStrategy customSessionAuthenticationStrategy() {
        return new CustomSessionAuthenticationStrategy();
    }

    @Bean
    public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {
        CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy = new CompositeSessionAuthenticationStrategy(
                Arrays.asList(csrfAuthenticationStrategy(), customSessionAuthenticationStrategy(), new SessionFixationProtectionStrategy(), new ChangeSessionIdAuthenticationStrategy()));
        return compositeSessionAuthenticationStrategy;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

//    @Bean
//    public WebMvcConfigurer webMvcConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
//                        .maxAge(3600);
//            }
//        };
//    }
}
