package com.yakiyo.medicine.medicine_info.dto.res;

import com.yakiyo.medicine.time.domain.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NextMedicineResDto {
    private Long hoursUntilNext;  // 다음 복용까지 남은 시간
    private String medicineName;  // 약 이름
    private TimePeriod period;    // 복용 시간대 (아침/점심/저녁)
    private LocalTime specificTime; // 구체적인 복용 시간
    private Boolean hasTakenMedicine; // 복용 여부
}
