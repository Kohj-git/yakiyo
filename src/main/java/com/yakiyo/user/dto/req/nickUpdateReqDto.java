package com.yakiyo.user.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class nickUpdateReqDto {
    private String googleId;
    private String nickname;
}
