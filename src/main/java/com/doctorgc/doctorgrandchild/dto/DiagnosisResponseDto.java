package com.doctorgc.doctorgrandchild.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DiagnosisResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DiagnosisStartDto {
        private Long diagnosisResultId;
        private Boolean isQuestion;
        private DiagnosisResultContentsDto content;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DiagnosisChatDto {
        private Boolean isQuestion;
        private DiagnosisResultContentsDto content;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DiagnosisResultContentsDto {
        private String  question;
        private List<String> options;
        private String result;
        private String hospitalName;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DiagnosisResultDto {
        private String content;
        private List<HospitalListDto> hospitalList;

    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HospitalListDto {
        private Long hospitalId;
        private String name;
        private String category;
        private String phoneNumber;
        private Long distance;
    }
}
