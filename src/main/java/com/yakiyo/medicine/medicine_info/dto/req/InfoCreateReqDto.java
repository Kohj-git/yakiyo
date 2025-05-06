package com.yakiyo.medicine.medicine_info.dto.req;

import com.yakiyo.medicine.days.domain.DayOfWeek;
import com.yakiyo.medicine.time.domain.TimePeriod;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoCreateReqDto {
    @Schema(example = "타이레놀", description = "약 이름")
    private String medicineName;

    @Schema(example = "false", description = "평생 복용 여부")
    private Boolean isLifetime;

    @Schema(example = "2025-05-06", description = "약 복용 시작일")
    private LocalDate startDate;

    @Schema(example = "2025-12-31", description = "약 복용 종료일 (평생 복용이 아닐 경우)")
    private LocalDate endDate;

    @Schema(example = "[\"MON\", \"WED\", \"FRI\"]", description = "복용 요일 정보")
    private List<DayOfWeek> daysOfWeek;

    private List<TimeMedicineDto> times;
    

    
    // 내부 DTO 클래스: 복용 시간 정보
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimeMedicineDto {
        @Schema(example = "MORNING", description = "복용 시간대 (MORNING, AFTERNOON, EVENING)")
        private TimePeriod period;

        @Schema(example = "09:00", description = "구체적인 시간 (HH:mm 형식)")
        private LocalTime specificTime;
    }


}
