package com.yakiyo.medicine.time.repo;

import com.yakiyo.medicine.time.domain.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepo extends JpaRepository<Time, Long> {
    // 기본 CRUD 기능 상속
}
