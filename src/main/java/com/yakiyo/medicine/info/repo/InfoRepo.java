package com.yakiyo.medicine.info.repo;

import com.yakiyo.medicine.info.domain.Info;
import com.yakiyo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoRepo extends JpaRepository<Info, Long> {

}
