package com.doctorgc.doctorgrandchild.repository;

import com.doctorgc.doctorgrandchild.entity.HealthReport;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import java.time.LocalDate;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {

    Optional<HealthReport> findByMemberAndDate(@NotNull Member member, @NotNull LocalDate date);

}
