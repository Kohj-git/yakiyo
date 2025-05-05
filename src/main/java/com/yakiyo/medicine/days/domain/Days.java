package com.yakiyo.medicine.days.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Days {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;  // 요일 (MON, TUE, WED, THU, FRI, SAT, SUN)
    

    public void updateDay(DayOfWeek day) {
        this.day = day;
    }
}
