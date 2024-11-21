package com.doctorgc.doctorgrandchild.service;

import com.doctorgc.doctorgrandchild.dto.UpdateMemberRequestDto;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import com.doctorgc.doctorgrandchild.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void updateMemberInfo(String email, UpdateMemberRequestDto requestDto){
        //DB에서 email로 member찾기
        Member member = memberRepository.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 유저가 없습니다"));
        //생년월일 나이로 변환
        String birthDate = requestDto.getBirthDate();
        if (birthDate.length() != 8){
            throw new IllegalArgumentException("유효하지 않은 생년월일 형식입니다");
        }
        long birthYear = Long.parseLong(birthDate.substring(0, 4)); // YYYY 슬라이싱 후 Long 변환
        long currentYear = LocalDate.now().getYear();
        Long age = (currentYear - birthYear) + 1;

        //멤버 정보 업데이트
        member.updateAge(age);
        member.updateSex(requestDto.getSex());
        member.updateMedicalConditions(requestDto.getMedicalConditions());
    }
}
