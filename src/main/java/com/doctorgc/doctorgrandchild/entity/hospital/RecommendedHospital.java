//package com.doctorgc.doctorgrandchild.entity.hospital;
//
//import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "recommended_hospital")
//public class RecommendedHospital {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "recommended_hospital_id")
//    private String id; // 추천 병원 ID (Primary Key)
//
//    @ManyToOne
//    @JoinColumn(name = "diagnosis_result_id", referencedColumnName = "diagnosis_result_id", nullable = false)
//    private DiagnosisResult diagnosisResult; // DiagnosisResult와 연관
//
//    // 추천 병원과 병원 상세정보를 매핑
//    @OneToOne
//    @JoinColumn(name = "hospital_id2", referencedColumnName = "hospital_id", nullable = false)
//    private Hospital hospital; // Hospital 테이블과 매핑
//}