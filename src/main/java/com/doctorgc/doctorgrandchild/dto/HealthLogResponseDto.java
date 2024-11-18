package com.doctorgc.doctorgrandchild.dto;

import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HealthLogResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberInfoDto {
        private String name;
        private String content;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HealthLogShortDto {
        private String content;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DiagnosisListDto {
        private List<ShortDiagnosisResultDto> diagnosisResults;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ShortDiagnosisResultDto {
        private Long diagnosisId;
        private LocalDate date;
        private String content;
    }
}
