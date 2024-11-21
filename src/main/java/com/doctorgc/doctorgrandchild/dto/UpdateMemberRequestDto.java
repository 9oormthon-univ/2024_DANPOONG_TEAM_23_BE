package com.doctorgc.doctorgrandchild.dto;

import com.doctorgc.doctorgrandchild.entity.member.Sex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRequestDto {
    @NotBlank(message = "생년월일은 필수 입력 값입니다.")
    @Pattern(regexp = "\\d{8}", message = "생년월일은 YYYYMMDD 형식이어야 합니다.")
    private String birthDate;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private Sex sex; // "male" 또는 "female" 예상

    @NotBlank(message = "의료 정보는 필수 입력 값입니다.")
    private String medicalConditions;
}
