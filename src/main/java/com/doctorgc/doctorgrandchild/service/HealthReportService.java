package com.doctorgc.doctorgrandchild.service;

import com.doctorgc.doctorgrandchild.dto.HealthChangesResponseDto.HealthChangesDto;
import com.doctorgc.doctorgrandchild.dto.HealthChangesResponseDto.ShortHealthChangesDto;
import com.doctorgc.doctorgrandchild.entity.HealthReport;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import com.doctorgc.doctorgrandchild.repository.HealthReportRepository;
import com.doctorgc.doctorgrandchild.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class HealthReportService {

    private final MemberRepository memberRepository;
    private final HealthReportRepository healthReportRepository;
    private final ClaudeService claudeService;

    public ShortHealthChangesDto getShortHealthReport(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        Optional<HealthReport> healthReport =
            healthReportRepository.findByMemberAndDate(member, LocalDate.now());
        if (healthReport.isPresent()) {
            return ShortHealthChangesDto.builder()
                .content(healthReport.get().getDisease())
                .build();
        }

        return claudeService.generateHealthChanges(member);
    }

    public HealthChangesDto getHealthReport(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        HealthReport healthReport =
            healthReportRepository.findByMemberAndDate(member, LocalDate.now()).orElseThrow();

        return HealthChangesDto.builder()
            .disease(healthReport.getDisease())
            .reportContent(healthReport.getReportContent())
            .build();
    }

}
