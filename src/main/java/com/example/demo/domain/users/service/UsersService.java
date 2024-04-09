package com.example.demo.domain.users.service;

import com.example.demo.domain.users.dto.UserDto;
import com.example.demo.domain.users.entity.Users;
import com.example.demo.domain.users.repository.UsersRepository;
import com.example.demo.utils.jwt.JwtToken;
import com.example.demo.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder encoder;


    @Transactional
    public String signup(UserDto userDto){
        Optional<Users> existingUser = usersRepository.findByUserId(userDto.getUserId());
        if(existingUser.isPresent()){
            return "User with this userId already exists.";
        }
        userDto.setPassword((encoder.encode(userDto.getPassword())));
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        userDto.setRoles(roles);
        usersRepository.save(Users.userBuild(userDto));
        return "Membership registration successful!";
    }

    @Transactional
    public JwtToken signIn(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }
}
