//package com.doctorgc.doctorgrandchild.controller;
//
//import com.doctorgc.doctorgrandchild.dto.HospitalResponseDto.HospitalDetailsDto;
//import com.doctorgc.doctorgrandchild.dto.HospitalResponseDto.RecommendedHospitalListDto;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/hospitals")
//@RestController
//public class HospitalController {
//
//    //병원 리스트 간략 조회 api
//    @Operation(summary = "병원 리스트 간략 조회", description ="진단결과 id를 보내주세요 추천병원 리스트를 최대 4개 반환")
//    @GetMapping("/recommend/{diagnosisResultId}")
//    public ResponseEntity<RecommendedHospitalListDto> getHospitalList(){
//
//    }
//
//
//    //병원 상세 조회 api
//    @Operation(summary = "병원 상세 조회", description ="병원 id를 보내주세요 병원에 대한 상세 정보 제공")
//    @GetMapping("/details/{hospitalId}")
//    public ResponseEntity<HospitalDetailsDto> getHospitalDetails(){
//
//    }
//
//}
