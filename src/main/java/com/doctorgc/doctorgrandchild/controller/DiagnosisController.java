package com.doctorgc.doctorgrandchild.controller;

import com.doctorgc.doctorgrandchild.dto.DiagnosisRequestDto.UserMessageDto;
import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisChatDto;
import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisResultDto;
import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisStartDto;
import com.doctorgc.doctorgrandchild.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @Operation(summary = "채팅 시작", description = "사용자가 진단하기를 눌렀을 때 서버에 진단 결과 객체를 생성")
    @PostMapping("/start")
    public ResponseEntity<DiagnosisStartDto> startDiagnosis(@AuthenticationPrincipal UserDetails userDetails,
        @RequestBody UserMessageDto userMessageDto) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(diagnosisService.startDiagnosis(email, userMessageDto));
    }

    @Operation(summary = "채팅 전송", description = "AI에게 사용자가 채팅 메시지 전송")
    @PatchMapping("/chat/{diagnosisResultId}")
    public ResponseEntity<DiagnosisChatDto> continueDiagnosis(@PathVariable Long diagnosisResultId,
        @AuthenticationPrincipal UserDetails userDetails, @RequestBody UserMessageDto userMessageDto) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(diagnosisService.continueDiagnosis(email, userMessageDto, diagnosisResultId));
    }

    @Operation(summary = "AI 진단결과 재조회", description = "AI가 내놓은 진단결과 재조회")
    @GetMapping("/results/{diagnosisResultId}")
    public ResponseEntity<DiagnosisResultDto> getDiagnosisResult(@PathVariable Long diagnosisResultId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisResult(diagnosisResultId));
    }

}