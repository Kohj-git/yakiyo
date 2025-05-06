package com.yakiyo.user.dto.res;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyInfoResDto {
    private String nickname;
    private String email;
}
