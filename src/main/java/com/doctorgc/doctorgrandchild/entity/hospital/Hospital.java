//package com.doctorgc.doctorgrandchild.entity.hospital;
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
//@Table(name = "hospital")
//public class Hospital {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "hospital_id")
//    private String id; // 병원 ID (Primary Key)
//
//    @Column(name = "name", nullable = false)
//    private String name; // 병원 이름
//
//    @Column(name = "category", nullable = false)
//    private String category; // 병원 카테고리
//
//    @Column(name = "address", nullable = false)
//    private String address; // 병원 주소
//
//    @Column(name = "road_address", nullable = false)
//    private String roadAddress; // 병원 도로명 주소
//
//    @Column(name = "distance", nullable = false)
//    private String distance; //현재 위치로부터 거리
//
//    @Column(name = "phone_number")
//    private String phoneNumber; // 병원 전화번호
//
//    @Column(name = "location_url")
//    private String locationUrl; //병원 상세url
//
//    @Column(name = "x")
//    private String x; //병원 x좌표
//
//    @Column(name = "y")
//    private String y; //병원 y좌표
//}
