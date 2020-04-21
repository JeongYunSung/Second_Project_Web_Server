package com.yunseong.secod_project_web_server.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object attribute = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String statusCode = String.valueOf(attribute);

        if(statusCode.equalsIgnoreCase(HttpStatus.UNAUTHORIZED.toString())) {
            return "redirect:/signin";
        }
        return "redirect:/";
    }

    @GetMapping("/deniedpage")
    public String duplicateLogin() {
        return "/deniedpage";
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
