package com.yakiyo.user.service;

import com.yakiyo.user.domain.User;
import com.yakiyo.user.dto.req.DeleteReqDto;
import com.yakiyo.user.dto.req.Fcm_updateReqDto;
import com.yakiyo.user.dto.req.LoginReqDto;
import com.yakiyo.user.dto.res.FcmResDto;
import com.yakiyo.user.dto.res.LoginResDto;
import com.yakiyo.user.dto.res.DeleteResDto;
import com.yakiyo.user.mapper.UserMapper;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    // 로그인 및 회원가입
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

    // FCM 토큰 업데이트
    @Transactional
    public FcmResDto updateFcmToken(Fcm_updateReqDto request) {
        // 구글ID로 사용자 조회
        Optional<User> userOptional = userRepo.findById(request.getGoogleId());
        
        if (userOptional.isPresent()) {
            // 사용자가 존재하면 FCM 토큰 업데이트
            User user = userOptional.get();
            user.updateFcmToken(request.getFcmToken());
            userRepo.save(user);
            return FcmResDto.builder()
                    .success(true)
                    .message("FCM 토큰 업데이트 완료")
                    .build();
        } else {
            // 사용자가 존재하지 않으면 오류 응답
            return FcmResDto.builder()
                    .success(false)
                    .message("유저를 찾을 수 없습니다")
                    .build();
        }
    }

    // 유저 삭제
    @Transactional
    public DeleteResDto deleteUser(DeleteReqDto request) {

        // 구글ID로 사용자 조회
        Optional<User> userOptional = userRepo.findById(request.getGoogleId());
        
        if (userOptional.isPresent()) {
            // 사용자가 존재하면 삭제 진행
            User user = userOptional.get();
            userRepo.delete(user);
            return DeleteResDto.builder()
                    .success(true)
                    .message("유저 삭제 완료")
                    .build();
        }
        else
        {
            // 사용자가 존재하지 않으면 오류 응답
            return DeleteResDto.builder()
                    .success(false)
                    .message("유저를 찾을 수 없습니다")
                    .build();
        }
    }
    
    
}
