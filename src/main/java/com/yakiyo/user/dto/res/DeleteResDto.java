package com.yakiyo.user.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteResDto {
    private boolean success;
    private String message;
}
