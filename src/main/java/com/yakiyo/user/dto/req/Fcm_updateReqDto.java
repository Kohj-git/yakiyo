package com.yakiyo.user.dto.req;

import lombok.Data;

@Data
public class Fcm_updateReqDto {
    private String googleId;
    private String fcmToken;
}
