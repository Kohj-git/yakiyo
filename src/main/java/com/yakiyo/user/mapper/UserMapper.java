package com.yakiyo.user.mapper;

import com.yakiyo.user.domain.User;
import com.yakiyo.user.dto.req.LoginReqDto;
import com.yakiyo.user.dto.res.LoginResDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    /**
     * LoginReqDto를 User 엔티티로 변환
     */
    public User toEntity(LoginReqDto dto) {
        return User.builder()
                .Id(dto.getGoogleId())
                .email(dto.getEmail())
                .name(dto.getName())
                .fcmToken(dto.getFcmToken())
                .build();
    }
    
    /**
     * User 엔티티를 LoginResDto로 변환
     */
    public LoginResDto toDto(User user) {
        return LoginResDto.builder()
                .googleId(user.getId())
                .name(user.getName())
                .fcmToken(user.getFcmToken())
                .email(user.getEmail())
                .build();
    }




}
