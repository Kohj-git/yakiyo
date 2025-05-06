package com.yakiyo.medicine.medicine_info.dto.res;

import com.yakiyo.medicine.days.domain.DayOfWeek;
import com.yakiyo.medicine.time.domain.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDetailResDto {
    private Long id;
    private String medicineName;
    private Boolean isLifetime;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DayOfWeek> daysOfWeek;
    private List<TimeInfo> times;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeInfo {
        private TimePeriod period;
        private LocalTime specificTime;
    }
}
