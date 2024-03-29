package com.example.demo.domain.users.service;

import com.example.demo.domain.users.dto.UserDto;
import com.example.demo.domain.users.entity.Users;
import com.example.demo.domain.users.repository.UsersRepository;
import com.example.demo.utils.jwt.JwtToken;
import com.example.demo.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder encoder;
    @Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 Users 반환 타입 지정 (자동으로 다운 캐스팅됨)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
        System.out.println(userId);
        System.out.println(usersRepository.findByUserId(userId));
        return usersRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(("해당유저없음")));
    }
    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(Users users) {
        return User.builder()
                .username(users.getUserId())
                .password(encoder.encode(users.getPassword()))
                .roles(users.getRole().toArray(new String[0]))
                .build();
    }
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
//        System.out.println(encoder.encode(password));
        System.out.println(authenticationToken);
        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("createToken");
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        System.out.println(jwtToken);

        return jwtToken;
    }
}
