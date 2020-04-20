package com.yunseong.secod_project_web_server.controller;

import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.common.security.CustomSessionAuthenticationStrategy;
import com.yunseong.secod_project_web_server.controller.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        model.addAttribute("user", new UserDTO());
        return "/signIn";
    }

    @GetMapping("/signup")
    public String getSignUp() {
        return "/signUp";
    }

    @PostMapping("/signin")
    public String postSignIn(HttpServletRequest request, Model model, UserDTO user) {
//        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/v1/members/siginin", entity, String.class);

        HttpEntity<UserDTO> entity = new HttpEntity<>(user);
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("http://localhost/test", entity, String.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            return "redirect:/signin";
        }

        if(this.customRegisterSessionAuthenticationStrategy.createSession(user.getUsername(),
                responseEntity.getHeaders().get(Util.ACCESS_TOKEN).get(0), request)) {
//            model.addAttribute("nextPage", "/");
//            model.addAttribute("msg", "다른 위치에 로그인이 되어있어 해당 연결을 강제로 종료합니다.");
//            return "forward:/deniedpage";
        }
        return "redirect:/";
    }

    @GetMapping("/signout")
    public String postSignOut(HttpServletRequest request) {
        if (request.getSession() != null) {
            this.customRegisterSessionAuthenticationStrategy.removeSession(request.getSession().getId());
        }
        return "redirect:/";
    }
}
