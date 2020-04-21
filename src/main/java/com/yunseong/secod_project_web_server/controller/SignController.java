package com.yunseong.secod_project_web_server.controller;

import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.common.security.CustomSessionAuthenticationStrategy;
import com.yunseong.secod_project_web_server.controller.dto.Page;
import com.yunseong.secod_project_web_server.controller.dto.SignInForm;
import com.yunseong.secod_project_web_server.controller.dto.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SignController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CustomSessionAuthenticationStrategy customRegisterSessionAuthenticationStrategy;

    @GetMapping("/signin")
    public String getSignIn(Model model) {
        model.addAttribute("signin", new SignInForm());
        return "/signIn";
    }

    @GetMapping("/signup")
    public String getSignUp(Model model) {
        model.addAttribute("signup", new SignUpForm());
        return "/signUp";
    }

    @GetMapping("/signout")
    public String postSignOut(HttpServletRequest request) {
        if (request.getSession() != null) {
            this.customRegisterSessionAuthenticationStrategy.removeSession(request.getSession().getId());
        }
        return "redirect:/";
    }

    @PostMapping("/signin")
    public String postSignIn(HttpServletRequest request, SignInForm signInForm) {
//        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("http://localhost/test", entity, String.class);
        HttpEntity<SignInForm> entity = new HttpEntity<>(signInForm);
        try {
            ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/members/signin", entity, String.class);

            HttpStatus statusCode = responseEntity.getStatusCode();
            if (!statusCode.is2xxSuccessful()) {
                return "redirect:/signin";
            }
            if(this.customRegisterSessionAuthenticationStrategy.createSession(signInForm.getUsername(),
                    responseEntity.getHeaders().get(Util.ACCESS_TOKEN).get(0), request)) {
                request.getSession().setAttribute("page", new Page("/", "다른 위치에 로그인이 되어있어 해당 연결을 강제로 종료합니다."));
                return "redirect:/deniedpage";
            }
        } catch(HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                System.out.println(e.getResponseBodyAsString() + " : 비밀번호가 틀렸습니다.");
            }
        }

        return "redirect:/";
    }

    @PostMapping("/signup")
    public String postSignUp(SignUpForm singUpForm) {
        HttpEntity<SignUpForm> entity = new HttpEntity<>(singUpForm);
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/members/signup", entity, String.class);

        HttpStatus status = responseEntity.getStatusCode();
        if (!status.is2xxSuccessful()) {
            return "redirect:/";
        }

        return "redirect:/signin";
    }
}
