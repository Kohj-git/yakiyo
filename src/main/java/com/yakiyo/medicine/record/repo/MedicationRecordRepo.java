package com.yakiyo.medicine.record.repo;

import com.yakiyo.medicine.medicine_info.domain.Medicine_info;
import com.yakiyo.medicine.record.domain.MedicationRecord;
import com.yakiyo.medicine.time.domain.Time;
import com.yakiyo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRecordRepo extends JpaRepository<MedicationRecord, Long> {
    // 특정 날짜의 복용 기록 조회
    List<MedicationRecord> findByUserAndDate(User user, LocalDate date);
    
    // 특정 약, 특정 시간의 복용 기록 조회
    Optional<MedicationRecord> findByUserAndMedicineAndTimeAndDate(
            User user, Medicine_info medicine, Time time, LocalDate date);
}
