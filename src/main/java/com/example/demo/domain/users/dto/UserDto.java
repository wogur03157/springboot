package com.example.demo.domain.users.dto;

import com.example.demo.domain.users.entity.Users;
import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userSeq;
    private String userId;
    private String password;
    private String userRule;
    private LocalDateTime createDate;

    public UserDto convertToDTO(Users users) {
        return UserDto.builder()
                .userId(users.getUserId())
                .password(users.getPassword())
                .userRule(users.getUserRule())
                .build();
    }
}
