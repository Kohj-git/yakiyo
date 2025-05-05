package com.yakiyo.user.service;

import com.yakiyo.user.domain.User;
import com.yakiyo.user.dto.req.FcmUpdateReqDto;
import com.yakiyo.user.dto.req.nickUpdateReqDto;
import com.yakiyo.user.dto.res.MyInfoResDto;
import com.yakiyo.exception.UserNotFoundException;
import com.yakiyo.user.mapper.UserMapper;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;


    // FCM 토큰 업데이트
    @Transactional
    public String updateFcmToken(String googleID,FcmUpdateReqDto request) {
        // 구글 ID로 사용자 조회 및 없으면 예외 발생
        User user = userRepo.findById(googleID)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));
        
        // 사용자가 존재하면 FCM 토큰 업데이트
        user.updateFcmToken(request.getFcmToken());
        userRepo.save(user);
        
        return "FCM 토큰 업데이트 완료";
    }

    // 유저 삭제
    @Transactional
    public String deleteUser(String googleId) {
        // 구글 ID로 사용자 조회 및 없으면 예외 발생
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));
        // 사용자가 존재하면 삭제 진행
        userRepo.delete(user);
        
        return "유저 삭제 완료";
    }


    //닉네임 설정
    public String updateNick(String googleId, nickUpdateReqDto request) {
        // 구글 ID로 사용자 조회 및 없으면 예외 발생
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));
        
        // 사용자가 존재하면 닉네임 업데이트 진행
        user.updateNickName(request.getNickname());
        userRepo.save(user);
        
        return "닉네임 설정 완료";
    }

    //마이페이지에서
    //닉네임, 이메일 불러오기
    public MyInfoResDto loadUserInfo(String googleId) {
        return userRepo.findById(googleId)
                .map(user -> MyInfoResDto.builder()
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .build())
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));
    }
}
