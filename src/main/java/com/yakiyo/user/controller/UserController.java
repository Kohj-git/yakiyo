package com.yakiyo.user.controller;

import com.yakiyo.user.dto.req.FcmUpdateReqDto;
import com.yakiyo.user.dto.req.LoginReqDto;
import com.yakiyo.user.dto.req.nickUpdateReqDto;
import com.yakiyo.user.dto.res.*;

import com.yakiyo.exception.UserNotFoundException;
import com.yakiyo.user.service.UserService;
import com.yakiyo.user.service.LoginService;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "사용자", description = "사용자 관리를 위한 API")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @Operation(
        summary = "로그인 및 회원가입",
        description = "구글 ID를 기준으로 로그인하거나 새로운 회원으로 가입합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = """
            {
                "id": "1123123",
                "name": "야기요",
                "email": "hong@gmail.com",
                "fcmToken": "fcm_token_example"
            }
            """))))
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto request) {
        return ResponseEntity.ok(loginService.login(request));
    }


    @PatchMapping("/fcm/{googleId}")
    public ResponseEntity<String> updateFcmToken(@PathVariable String googleId,
            @RequestBody FcmUpdateReqDto request) {
        return ResponseEntity.ok(userService.updateFcmToken(googleId,request));
    }


    @DeleteMapping("/{googleId}")
    public ResponseEntity<String> deleteUser(@PathVariable String googleId) {
        return ResponseEntity.ok(userService.deleteUser(googleId));
    }


    @PatchMapping("/nick/{googleId}")
    public ResponseEntity<String> updateNickname( @PathVariable String googleId,
            @RequestBody nickUpdateReqDto request) {
        return ResponseEntity.ok(userService.updateNick(googleId, request));
    }

    //마이페이지
    //닉네임,이메일 필요
    //닉네임 없을시 null 감
    @GetMapping("/info/{googleId}")
    public ResponseEntity<MyInfoResDto> loadUserInfo(@PathVariable String googleId) {
        return ResponseEntity.ok(userService.loadUserInfo(googleId));
    }


}
