package com.yakiyo.medicine.medicine_info.dto.res;

import com.yakiyo.medicine.time.domain.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineListResDto {
    private Long id;
    private String medicineName;
    private List<TimePeriod> periods;  // 복용 시간대 (MORNING, AFTERNOON, EVENING)
}
