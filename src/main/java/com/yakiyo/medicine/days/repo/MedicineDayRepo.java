package com.yakiyo.medicine.days.repo;

import com.yakiyo.medicine.days.domain.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineDayRepo extends JpaRepository<Days, Long> {

}
