package com.doctorgc.doctorgrandchild.controller;

import com.doctorgc.doctorgrandchild.dto.KakaoUserInfoResponseDto;
import com.doctorgc.doctorgrandchild.dto.LoginResponseDto;
import com.doctorgc.doctorgrandchild.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/{code}")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam("code") String code){
        //프론트로부터 받은 code로 kakao에 요청해 accesstoken 받기
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        //kakao로부터 받은 accesstoken으로 유저정보 받기
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        //받은 유저정보로 자체 db를 보고 있는 회원이면 로그인, 없으면 회원가입 후 로그인
        LoginResponseDto memberLoginResponse = kakaoService.kakaoUserLogin(userInfo);

        //memberLoginResponse를 어떻게 담아 보낼것인가?


        return ResponseEntity.ok(memberLoginResponse);
    }

}
