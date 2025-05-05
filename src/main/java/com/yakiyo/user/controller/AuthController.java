package com.yakiyo.user.controller;

import com.yakiyo.user.dto.req.DeleteReqDto;
import com.yakiyo.user.dto.req.Fcm_updateReqDto;
import com.yakiyo.user.dto.req.LoginReqDto;
import com.yakiyo.user.dto.res.FcmResDto;
import com.yakiyo.user.dto.res.LoginResDto;

import com.yakiyo.user.dto.res.DeleteResDto;
import com.yakiyo.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인 및 회원가입 (구글 ID 기준)
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto request) {
        return ResponseEntity.ok(authService.login(request));
    }


    // FCM 토큰 업데이트
    //로그인/회원가입 시: fcmToken 같이 보내서 저장/갱신
    // 토큰이 갱신될 때마다: Flutter 에서 서버로 fcmToken 업데이트
    @PatchMapping("/fcm_update")
    public ResponseEntity<FcmResDto> updateFcmToken(@RequestBody Fcm_updateReqDto request) {
        return ResponseEntity.ok(authService.updateFcmToken(request));
    }


    // 유저 삭제 (탈퇴)
    @DeleteMapping("")
    public ResponseEntity<DeleteResDto> deleteUser(@RequestBody DeleteReqDto request) {
        return ResponseEntity.ok(authService.deleteUser(request));
    }


}
