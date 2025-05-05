package com.yakiyo.user.dto.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class nickUpdateResDto {
    private boolean success;
    private String message;

}
