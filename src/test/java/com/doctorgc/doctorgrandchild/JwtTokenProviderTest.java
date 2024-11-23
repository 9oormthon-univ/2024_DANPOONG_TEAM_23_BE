package com.doctorgc.doctorgrandchild;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.doctorgc.doctorgrandchild.config.auth.CustomUserDetailService;
import com.doctorgc.doctorgrandchild.config.auth.UserDetailsImpl;
import com.doctorgc.doctorgrandchild.config.jwt.JwtTokenDto;
import com.doctorgc.doctorgrandchild.config.jwt.JwtTokenProvider;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import com.doctorgc.doctorgrandchild.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {



//    private
//   private @BeforeEach
//    void setUp() {
//        // 테스트용 secretKey 설정
//        jwtTokenProvider.setSecretKey("test-secret-key");
//        jwtTokenProvider.init(); // Base64로 인코딩
//    }
    String secretKey = "";


    @Test
    void generateAndValidateAccessToken() {

        MemberRepository memberRepository = mock(MemberRepository.class);

        Member member = Member.builder()
                                .id(1L)
                                .email("test1234@kakao.com")
                                .name("woojoo")
                                .profileImage(null)
                                .isActive(true)
                                .build();

        lenient().when(memberRepository.save(any(Member.class))).thenReturn(member);
        lenient().when(memberRepository.findByEmail("test1234@kakao.com")).thenReturn(java.util.Optional.of(member));


        UserDetailsImpl userDetails = new UserDetailsImpl(member.getEmail());

        //Mock CustomUserDetailService 생성
        CustomUserDetailService userDetailsService = new CustomUserDetailService(memberRepository);

        // JwtTokenProvider 초기화
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService);



        // Access Token 생성
        JwtTokenDto tokens = jwtTokenProvider.generateToken(userDetails);

        // 개발용 토큰 출력
        System.out.println("Generated Development Access Token: " + tokens.getAccessToken());
        System.out.println("Generated Development Refresh Token: " + tokens.getRefreshToken());


        // Access Token 검증
        String parsedEmail = jwtTokenProvider.getUserPk(tokens.getAccessToken());

        // 검증된 클레임 확인
        assertEquals(member.getEmail(),parsedEmail);
    }
}
