package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dto.LoginDto;
import com.heima.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")

public class ApLoginUserController {
    @Autowired
    private ApUserService userService;
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }
}
