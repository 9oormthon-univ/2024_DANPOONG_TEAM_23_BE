package com.doctorgc.doctorgrandchild.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HospitalResponseDto {

    //추천병원 리스트
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecommendedHospitalListDto {
        private List<RecommendedHospitalDto> recommendedHospitalList;

    }

    //추천병원
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecommendedHospitalDto {
        private Long hospitalId;
        private String name;
        private String category;
        private String phoneNumber;
        private String distance;
    }


    //추천병원 상세
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HospitalDetailsDto {
        private Long hospitalId;
        private String name;
        private String category;
        private String address;
        private String roadAddress;
        private String phoneNumber;
        private String distance;
        private String locationUrl;
        private String x;
        private String y;

    }


}
