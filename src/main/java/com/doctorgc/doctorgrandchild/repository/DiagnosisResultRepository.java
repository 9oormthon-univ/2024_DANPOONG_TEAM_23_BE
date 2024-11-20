package com.doctorgc.doctorgrandchild.repository;

import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisResultRepository extends JpaRepository<DiagnosisResult, Long> {

    List<DiagnosisResult> findByMemberAndDateBetween(
        @NotNull Member member, @NotNull LocalDate startDate, @NotNull LocalDate endDate);

    Optional<DiagnosisResult> findByMemberAndDate(
        @NotNull Member member, @NotNull LocalDate date);
}
