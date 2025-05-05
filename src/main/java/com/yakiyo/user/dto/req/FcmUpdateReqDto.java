package com.yakiyo.user.dto.req;

import lombok.Data;

@Data
public class FcmUpdateReqDto {
    private String googleId;
    private String fcmToken;
}
