package com.yakiyo.user.service;

import com.yakiyo.user.domain.User;
import com.yakiyo.user.dto.req.LoginReqDto;
import com.yakiyo.user.dto.res.LoginResDto;
import com.yakiyo.user.mapper.UserMapper;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Transactional
    public LoginResDto login(LoginReqDto request) {
        // 구글ID로 사용자 조회
        Optional<User> userOptional = userRepo.findById(request.getGoogleId());

        if (userOptional.isPresent()) {
            // 기존 유저: 필요한 정보만 업데이트
            User user = userOptional.get();
            user.updateFcmToken(request.getFcmToken());
            user.updateName(request.getName());
            user.updateEmail(request.getEmail());
            userRepo.save(user);
            return userMapper.toDto(user);
        } else {

            // 신규 유저: 생성만 하고 추가 업데이트 없음
            User newUser = userMapper.toEntity(request);  // 직접 호출
            userRepo.save(newUser);
            return userMapper.toDto(newUser);
        }

    }

}
