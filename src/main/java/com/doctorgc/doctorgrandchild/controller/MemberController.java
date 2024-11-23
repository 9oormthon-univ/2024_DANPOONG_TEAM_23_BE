package com.doctorgc.doctorgrandchild.controller;

import com.doctorgc.doctorgrandchild.config.auth.UserDetailsImpl;
import com.doctorgc.doctorgrandchild.dto.ApiResponseDto;
import com.doctorgc.doctorgrandchild.dto.UpdateMemberRequestDto;
import com.doctorgc.doctorgrandchild.service.MemberService;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    @Operation(summary = "추가 정보 입력", description ="생년월일,성별,기저질환을 입력해주세요")
    @PutMapping("/fill")
    public ResponseEntity<ApiResponseDto> updateMemberInfo(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Validated UpdateMemberRequestDto requestDto, HttpServletRequest request) {
        String email = userDetails.getUsername();



        // 서비스 호출
        memberService.updateMemberInfo(email, requestDto);

        // 성공 응답 반환
        return ResponseEntity.ok(new ApiResponseDto(
                HttpStatus.OK.value(),
                "회원 정보가 성공적으로 업데이트되었습니다."));
    }


}
