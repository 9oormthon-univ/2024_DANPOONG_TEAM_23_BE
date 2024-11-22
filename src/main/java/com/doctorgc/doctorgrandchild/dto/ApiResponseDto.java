package com.doctorgc.doctorgrandchild.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    private int status; // HTTP 상태 코드
    private String message; // 응답 메시지
}