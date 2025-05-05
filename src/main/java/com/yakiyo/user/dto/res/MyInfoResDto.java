package com.yakiyo.user.dto.res;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyInfoResDto {
    private String nickname;
    private String email;
}
