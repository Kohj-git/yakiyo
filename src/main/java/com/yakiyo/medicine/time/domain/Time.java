package com.yakiyo.medicine.time.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 아침, 점심, 저녁 등 복용 시간대
    @Enumerated(EnumType.STRING)
    private TimePeriod period;
    
    // 구체적인 시간 (HH:MM)
    private LocalTime specificTime;
    

    
    // 시간대 업데이트
    public void updatePeriod(TimePeriod period) {
        this.period = period;
    }
    
    // 구체적인 시간 업데이트
    public void updateSpecificTime(LocalTime specificTime) {
        this.specificTime = specificTime;
    }
}
