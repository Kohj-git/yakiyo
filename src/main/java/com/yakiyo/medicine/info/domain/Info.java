package com.yakiyo.medicine.info.domain;

import com.yakiyo.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String medicineName;

    private Boolean isLifetime;

    private LocalDate startDate;
    private LocalDate endDate;

    //@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    //private List<ScheduleDay> days = new ArrayList<>();

    //@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    //private List<ScheduleTime> times = new ArrayList<>();
}
