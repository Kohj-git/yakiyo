package com.yakiyo.medicine.medicine_info.dto.req;

import com.yakiyo.medicine.days.domain.DayOfWeek;
import com.yakiyo.medicine.time.domain.TimePeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoUpdateReqDto {
    @Schema(example = "타이레놀", description = "약 이름")
    private String medicineName;

    @Schema(example = "[\"MON\", \"WED\", \"FRI\"]", description = "복용 요일 정보")
    private List<DayOfWeek> daysOfWeek;

    private List<TimeMedicineDto> times;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeMedicineDto {
        @Schema(example = "MORNING", description = "복용 시간대 (MORNING, AFTERNOON, EVENING)")
        private TimePeriod period;

        @Schema(example = "09:00", description = "구체적인 시간 (HH:mm 형식)")
        private LocalTime specificTime;
    }
}
