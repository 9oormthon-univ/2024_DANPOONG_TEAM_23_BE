package com.doctorgc.doctorgrandchild.config.jwt;

import jakarta.annotation.PostConstruct;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    //객체 초기화 및 secretKey Base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

//    //토큰 생성
//    public String generateToken(String userPk) {
//        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
//        Date now = new Date();
//        return Jwts.builder()
//                       .setClaims(claims) // 정보 저장
//                       .setIssuedAt(now) // 토큰 발행 시간 정보
//                       .setExpiration(new Date(now.getTime() + (30 * 60 * 1000L))) // 토큰 유효시각 설정 (30분)
//                       .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘과, secret 값
//                       .compact();
//    }


}
