package com.yakiyo.medicine.info.service;

import com.yakiyo.exception.UserNotFoundException;
import com.yakiyo.medicine.info.domain.Info;
import com.yakiyo.medicine.info.repo.InfoRepo;
import com.yakiyo.user.domain.User;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepo infoRepo;
    private final UserRepo userRepo;




}
