package com.example.demo.domain.users.controller;

import com.example.demo.domain.users.dto.UserDto;
import com.example.demo.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public String signup(@RequestBody UserDto userDto){
        System.out.println("adsf");
        return usersService.signup(userDto);
    }
    @GetMapping("/test")
    public String test(){
        System.out.println("test");
        return "test";
    }

}
