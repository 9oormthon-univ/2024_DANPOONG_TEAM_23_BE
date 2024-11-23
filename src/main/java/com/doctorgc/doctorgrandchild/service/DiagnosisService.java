package com.doctorgc.doctorgrandchild.service;

import com.doctorgc.doctorgrandchild.dto.DiagnosisRequestDto.SelfDiagnosisDto;
import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisChatDto;
import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisStartDto;
import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import com.doctorgc.doctorgrandchild.repository.DiagnosisResultRepository;
import com.doctorgc.doctorgrandchild.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DiagnosisService {

    private final MemberRepository memberRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final ClaudeService claudeService;

    public DiagnosisStartDto startDiagnosis(String email, SelfDiagnosisDto selfDiagnosisDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        DiagnosisResult diagnosisResult = DiagnosisResult.builder()
            .content("")
            .shortContent("")
            .date(LocalDate.now())
            .member(member)
            .questionCount(0)
            .userInput(selfDiagnosisDto.getSelfDiagnosis())
            .hospitalCategory("")
            .build();
        DiagnosisResult result = diagnosisResultRepository.save(diagnosisResult);
        DiagnosisChatDto chatResult = claudeService.generateDiagnosis(member, diagnosisResult);
        return DiagnosisStartDto.builder()
            .diagnosisResultId(result.getId())
            .build();
    }
}
