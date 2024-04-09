package com.example.demo.domain.users.dto;

import com.example.demo.domain.users.entity.Users;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userSeq;
    private String userId;
    private String password;
    private String role;
    private List<String> roles = new ArrayList<>();
    private LocalDateTime createDate;

    public UserDto convertToDTO(Users users) {
        return UserDto.builder()
                .userId(users.getUserId())
                .password(users.getPassword())
                .role(users.getRole())
                .build();
    }
}
