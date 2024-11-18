package com.doctorgc.doctorgrandchild.repository;

import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisResultRepository extends JpaRepository<Long, DiagnosisResult> {

    List<DiagnosisResult> findByUserIdAndDateBetween
        (@NotNull Member member, @NotNull LocalDate startDate, @NotNull LocalDate endDate);
    Optional<DiagnosisResult> findByUserIdAndDate(@NotNull Member member, @NotNull LocalDate date);


}
