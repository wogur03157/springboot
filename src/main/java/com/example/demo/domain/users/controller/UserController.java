package com.example.demo.domain.users.controller;

import com.example.demo.domain.users.dto.UserDto;
import com.example.demo.domain.users.service.UsersService;
import com.example.demo.utils.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public String signup(@RequestBody UserDto userDto){
        return usersService.signup(userDto);
    }
    @PostMapping("/signin")
    public JwtToken signin(@RequestBody UserDto userDto){
        return usersService.signIn(userDto.getUserId(),userDto.getPassword());
    }
    @GetMapping("/test")
    public String test(){
        System.out.println("test");
        return "test";
    }

}
