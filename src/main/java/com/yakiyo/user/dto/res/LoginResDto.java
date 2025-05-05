package com.yakiyo.user.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResDto {
    private String googleId;
    private String name;
    private String email;
    private String fcmToken;
}
