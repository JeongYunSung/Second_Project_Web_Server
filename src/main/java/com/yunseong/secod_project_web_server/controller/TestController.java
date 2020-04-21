package com.yunseong.secod_project_web_server.controller;

import com.yunseong.secod_project_web_server.controller.dto.SignInForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @PostMapping("/test")
    public ResponseEntity test(@RequestBody SignInForm userDTO) {
        return ResponseEntity.ok().header("X-AUTH-TOKEN", "123465465").body(userDTO);
    }
}
