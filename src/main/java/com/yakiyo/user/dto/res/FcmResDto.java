package com.yakiyo.user.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FcmResDto {
    private boolean success;
    private String message;
}
