package com.doctorgc.doctorgrandchild.entity;

import com.doctorgc.doctorgrandchild.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
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
public class DiagnosisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String shortContent;

    @Column(nullable = false)
    private String hospitalCategory;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String userInput;

    @Column(nullable = false)
    private int questionCount;

    public void setContent(String content) {this.content = content;}

    public void setShortContent(String shortContent) {this.shortContent = shortContent;}

    public void setHospitalCategory(String hospitalCategory) {this.hospitalCategory = hospitalCategory;}

    public void setUserInput(String userInput) {this.userInput = userInput;}

    public void setQuestionCount(int questionCount) {this.questionCount = questionCount;}

}
