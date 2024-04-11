package com.example.demo.utils.jwt;

import com.example.demo.domain.users.entity.Users;
import com.example.demo.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final BCryptPasswordEncoder encoder;
    private final UsersRepository usersRepository;
    @Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 Users 반환 타입 지정 (자동으로 다운 캐스팅됨)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
            return usersRepository.findByUserId(userId)
                    .map(this::createUserDetails)
                    .orElseThrow(() -> new UsernameNotFoundException(("해당유저없음")));
    }

    private UserDetails createUserDetails(Users users) {
        return User.builder()
                .username(users.getUserId())
                .password(users.getPassword())
                .roles(users.getRole().toArray(new String[0]))
                .build();
    }
}
