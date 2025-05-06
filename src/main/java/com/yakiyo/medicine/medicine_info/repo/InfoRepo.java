package com.yakiyo.medicine.medicine_info.repo;

import com.yakiyo.medicine.medicine_info.domain.Medicine_info;
import com.yakiyo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoRepo extends JpaRepository<Medicine_info, Long> {
    // 사용자별 약 정보 조회
    List<Medicine_info> findByUser(User user);
}
