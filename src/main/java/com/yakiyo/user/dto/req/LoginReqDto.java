package com.yakiyo.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginReqDto {
    @NotNull(message = "구글 ID는 필수입니다")
    private String id; // 구글 sub
    @NotNull(message = "이름은 필수입니다")
    private String name; //사용자 이름
    @NotNull(message = "이메일은 필수입니다")
    private String email; //사용자 이메일
    private String fcmToken; //fcm 토큰 (선택사항)
}
