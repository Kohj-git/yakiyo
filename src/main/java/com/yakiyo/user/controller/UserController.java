package com.yakiyo.user.controller;

import com.yakiyo.user.dto.req.DeleteReqDto;
import com.yakiyo.user.dto.req.FcmUpdateReqDto;
import com.yakiyo.user.dto.req.LoginReqDto;
import com.yakiyo.user.dto.req.nickUpdateReqDto;
import com.yakiyo.user.dto.res.FcmResDto;
import com.yakiyo.user.dto.res.LoginResDto;

import com.yakiyo.user.dto.res.DeleteResDto;
import com.yakiyo.user.service.UserService;
import com.yakiyo.user.service.LoginService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    // 로그인 및 회원가입 (구글 ID 기준)
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto request) {
        return ResponseEntity.ok(loginService.login(request));
    }


    // FCM 토큰 업데이트
    //로그인/회원가입 시: fcmToken 같이 보내서 저장/갱신
    // 토큰이 갱신될 때마다: Flutter 에서 서버로 fcmToken 업데이트
    @PatchMapping("/fcm")
    public ResponseEntity<FcmResDto> updateFcmToken(@RequestBody FcmUpdateReqDto request) {
        return ResponseEntity.ok(userService.updateFcmToken(request));
    }


    // 유저 삭제 (탈퇴)
    @DeleteMapping("")
    public ResponseEntity<DeleteResDto> deleteUser(@RequestBody DeleteReqDto request) {
        return ResponseEntity.ok(userService.deleteUser(request));
    }

    @PatchMapping("/nick")
    public ResponseEntity<?> updateNickname(@RequestBody nickUpdateReqDto request){
        return ResponseEntity.ok(userService.updateNick(request));

    }


}
