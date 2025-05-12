package com.yakiyo.medicine.record.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarResDto {
    private Integer year;
    private Integer month;
    private List<DayStatus> days;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayStatus {
        private Integer date;
        private Status status;
    }

    public enum Status {
        RED,    // 약을 먹어야 했지만 하나라도 안 먹음
        GREEN,  // 먹어야 할 약을 모두 먹음
        BLACK,  // 먹을 약이 없는 날
        GRAY    // 미래의 복용 예정일
    }
}
