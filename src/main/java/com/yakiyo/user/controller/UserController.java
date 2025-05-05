package com.yakiyo.user.controller;

import com.yakiyo.user.dto.req.FcmUpdateReqDto;
import com.yakiyo.user.dto.req.LoginReqDto;
import com.yakiyo.user.dto.req.nickUpdateReqDto;
import com.yakiyo.user.dto.res.*;

import com.yakiyo.exception.UserNotFoundException;
import com.yakiyo.user.service.UserService;
import com.yakiyo.user.service.LoginService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PatchMapping("/fcm/{googleId}")
    public ResponseEntity<String> updateFcmToken(@PathVariable String googleId,
            @RequestBody FcmUpdateReqDto request) {
        try {
            return ResponseEntity.ok(userService.updateFcmToken(googleId,request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    // 유저 삭제 (탈퇴)
    @DeleteMapping("/{googleId}")
    public ResponseEntity<String> deleteUser(@PathVariable String googleId) {
        try {
            return ResponseEntity.ok(userService.deleteUser(googleId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //유저 닉네임 설정
    @PatchMapping("/nick/{googleId}")
    public ResponseEntity<String> updateNickname( @PathVariable String googleId,
            @RequestBody nickUpdateReqDto request) {
        try {
            return ResponseEntity.ok(userService.updateNick(googleId, request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //마이페이지
    //닉네임,이메일 필요
    //닉네임 없을시 null로 감
    @GetMapping("/info/{googleId}")
    public ResponseEntity<MyInfoResDto> loadUserInfo(@PathVariable String googleId) {
        try {
            return ResponseEntity.ok(userService.loadUserInfo(googleId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
