package com.yakiyo.user.service;

import com.yakiyo.user.domain.User;
import com.yakiyo.user.dto.req.DeleteReqDto;
import com.yakiyo.user.dto.req.FcmUpdateReqDto;
import com.yakiyo.user.dto.req.nickUpdateReqDto;
import com.yakiyo.user.dto.res.FcmResDto;
import com.yakiyo.user.dto.res.DeleteResDto;
import com.yakiyo.user.dto.res.nickUpdateResDto;
import com.yakiyo.user.mapper.UserMapper;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;


    // FCM 토큰 업데이트
    @Transactional
    public FcmResDto updateFcmToken(FcmUpdateReqDto request) {
        // 구글 ID로 사용자 조회
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

        // 구글 ID로 사용자 조회
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


    //닉네임 설정
    public nickUpdateResDto updateNick(nickUpdateReqDto request) {

        Optional<User> userOptional = userRepo.findById(request.getGoogleId());

        if (userOptional.isPresent()) {
            // 사용자가 존재하면 삭제 진행
            User user = userOptional.get();
            user.updateNickName(request.getNickname());
            userRepo.save(user);

            return nickUpdateResDto.builder()
                    .success(true)
                    .message("닉네임 설정 완료")
                    .build();
        }
        else
        {
            // 사용자가 존재하지 않으면 오류 응답
            return nickUpdateResDto.builder()
                    .success(false)
                    .message("유저를 찾을 수 없습니다")
                    .build();
        }

    }
    
}
