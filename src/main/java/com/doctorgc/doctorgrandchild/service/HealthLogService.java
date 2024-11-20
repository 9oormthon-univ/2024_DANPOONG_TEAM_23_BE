package com.doctorgc.doctorgrandchild.service;

import com.doctorgc.doctorgrandchild.dto.HealthLogResponseDto.DiagnosisListDto;
import com.doctorgc.doctorgrandchild.dto.HealthLogResponseDto.MemberInfoDto;
import com.doctorgc.doctorgrandchild.dto.HealthLogResponseDto.ShortDiagnosisResultDto;
import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import com.doctorgc.doctorgrandchild.repository.DiagnosisResultRepository;
import com.doctorgc.doctorgrandchild.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class HealthLogService {

    private final MemberRepository memberRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;

    public MemberInfoDto getInfo(String email, LocalDate date) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        Optional<DiagnosisResult> diagnosisResult =
            diagnosisResultRepository.findByMemberAndDate(member, date);
        String content = "";
        if (diagnosisResult.isPresent()) {
            content = diagnosisResult.get().getShortContent();
        }

        return MemberInfoDto.builder()
            .name(member.getName())
            .content(content)
            .build();
    }

    public DiagnosisListDto getDiagnosisLists(String email, YearMonth date) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        int year = date.getYear();
        int month = date.getMonthValue();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month % 12 + 1, 1);

        List<DiagnosisResult> diagnosisResults = diagnosisResultRepository
            .findByMemberAndDateBetween(member, startDate, endDate);

        List<ShortDiagnosisResultDto> shortDiagnosisLists = new ArrayList<>();

        for (DiagnosisResult diagnosisResult : diagnosisResults) {
            ShortDiagnosisResultDto shortResult = ShortDiagnosisResultDto.builder()
                .content(diagnosisResult.getShortContent())
                .date(diagnosisResult.getDate())
                .diagnosisId(diagnosisResult.getId())
                .build();
            shortDiagnosisLists.add(shortResult);
        }

        return DiagnosisListDto.builder()
            .diagnosisResults(shortDiagnosisLists)
            .build();
    }
}
