package com.yakiyo.medicine.medicine_info.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yakiyo.medicine.days.domain.Days;
import com.yakiyo.medicine.time.domain.Time;
import com.yakiyo.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine_info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String medicineName;

    private Boolean isLifetime;

    private LocalDate startDate;
    private LocalDate endDate;
    
    //복용요일
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "info_id")
    @JsonIgnore
    @Builder.Default
    private List<Days> days = new ArrayList<>();
    
    // 복용 시간
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "info_id")
    @JsonIgnore
    @Builder.Default
    private List<Time> times = new ArrayList<>();
    
    // 요일 추가 메서드
    public void addDay(Days day) {
        days.add(day);
    }
    
    // 요일 제거 메서드
    public void removeDay(Days day) {
        days.remove(day);
    }
    
    // 시간 추가 메서드
    public void addTime(Time time) {
        times.add(time);
    }
    
    // 시간 제거 메서드
    public void removeTime(Time time) {
        times.remove(time);
    }

    // 사용자 업데이트 메서드
    public void updateUser(User user) {
        this.user = user;
    }

    // 약 이름 업데이트 메서드
    public void updateMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    // 평생 복용 여부 업데이트 메서드
    public void updateIsLifetime(Boolean isLifetime) {
        this.isLifetime = isLifetime;
    }

    // 시작일 업데이트 메서드
    public void updateStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // 종료일 업데이트 메서드
    public void updateEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
