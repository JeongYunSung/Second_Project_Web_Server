package com.yunseong.secod_project_web_server.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

        return new RestTemplate(factory);
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
