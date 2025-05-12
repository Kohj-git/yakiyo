package com.yakiyo.medicine.medicine_info.dto.res;

import com.yakiyo.medicine.time.domain.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NextMedicineResDto {
    private long minutesUntilNext;
    private String medicineName;
    private List<TimeStatus> todayTimes;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeStatus {
        private TimePeriod period;
        private LocalTime specificTime;
        private boolean hasTakenMedicine;
    }
}
