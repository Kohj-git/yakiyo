package com.yakiyo.medicine.record.dto.res;

import com.yakiyo.medicine.time.domain.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakeMedicineResDto {
    private boolean allTaken;          // 모든 시간대 복용 완료 여부
    private TimePeriod takenTime;     // 체크한 복용 시간대 (MORNING/AFTERNOON/EVENING), allTaken이 true면 null
    private LocalDateTime takenAt;     // 실제 체크한 시간, allTaken이 true면 null
}
