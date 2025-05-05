package com.yakiyo.user.dto.req;

import lombok.Data;

@Data
public class LoginReqDto {
    private String googleId; // 구글 sub
    private String name; //사용자 이름
    private String email; //사용자 이메일
    private String fcmToken; //fcm 토큰
}

