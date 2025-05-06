package com.yakiyo.medicine.record.domain;

import com.yakiyo.medicine.medicine_info.domain.Medicine_info;
import com.yakiyo.medicine.time.domain.Time;
import com.yakiyo.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 복용한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine_info medicine;  // 복용한 약

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;  // 복용 시간 정보 (MORNING/AFTERNOON/EVENING + specificTime)

    private LocalDate date;  // 복용한 날짜

    private boolean taken;  // 복용 여부

    // 복용 여부 업데이트
    public void updateTaken(boolean taken) {
        this.taken = taken;
    }
}
