package com.doctorgc.doctorgrandchild.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; //email로 사용자 식별

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = true)
    private Long age;

    @Column(nullable = true)
    private String medicalConditions;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    //credential? token 저장할 거 있어야 (인가코드를통해나온토큰 저장해야) -> 탈퇴구현위해서

    @Builder
    public Member(String email, String name, String profileImage, boolean isActive){
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.isActive = isActive;
    }

    public void updateAge(Long age) {
        this.age = age;
    }

    public void updateMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public void updateSex(Sex sex) {
        this.sex = sex;
    }
}
