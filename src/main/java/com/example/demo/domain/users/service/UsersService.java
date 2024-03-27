package com.example.demo.domain.users.service;

import com.example.demo.domain.users.dto.UserDto;
import com.example.demo.domain.users.entity.Users;
import com.example.demo.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder encoder;
    @Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 Users 반환 타입 지정 (자동으로 다운 캐스팅됨)
    public Users loadUserByUsername(String userId) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
        System.out.println("loadUserByUsername");
        return usersRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException((userId)));
    }
    public String signup(UserDto userDto){
        Optional<Users> existingUser = usersRepository.findByUserId(userDto.getUserId());
        if(existingUser.isPresent()){
            return "User with this userId already exists.";
        }
        userDto.setPassword((encoder.encode(userDto.getPassword())));
        System.out.println(userDto);
        usersRepository.save(Users.userBuild(userDto));
        return "Membership registration successful!";
    }
}
