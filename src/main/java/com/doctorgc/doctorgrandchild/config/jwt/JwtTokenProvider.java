package com.doctorgc.doctorgrandchild.config.jwt;



import static org.hibernate.query.sqm.tree.SqmNode.log;

import com.doctorgc.doctorgrandchild.config.auth.CustomUserDetailService;
import com.doctorgc.doctorgrandchild.config.auth.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final CustomUserDetailService customUserDetailService;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        init(); // Base64로 인코딩
    }

    //객체 초기화 및 secretKey Base64로 인코딩
    @PostConstruct //protected로 바꿔야
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //토큰 생성 (access, refresh 둘 다)
    public JwtTokenDto generateToken(UserDetailsImpl userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername()); // getUsername이 가지고오는 것은 email
        Date now = new Date();

        //accessToken
        String accessToken = Jwts.builder()
                                     .setClaims(claims)
                                     .setIssuedAt(now)
                                     .setExpiration(new Date(now.getTime() + (100 * 60 * 60 *24 * 7)))
                                     .signWith(SignatureAlgorithm.HS256, secretKey)
                                     .compact();
        //refreshToken
        String refreshToken = Jwts.builder()
                                      .setClaims(claims)
                                      .setExpiration(new Date(now.getTime() + (1000 * 60 * 60 *24 * 7)))
                                      .signWith(SignatureAlgorithm.HS256,secretKey)
                                      .compact();


        return JwtTokenDto.builder()
                       .grantType("Bearer")
                       .accessToken(accessToken)
                       .refreshToken(refreshToken)
                       .build();
    }

    //인증 정보 조회
    public Authentication getAuthentication(String token) {
        String email = this.getUserPk(token);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰 유효성, 만료일자 확인
    public boolean validateToken(String token) {
        try {

            Jws<Claims> claims = Jwts.parser()
                                         .setSigningKey(secretKey)
                                         .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException | MalformedJwtException e) {
            // 예외를 그대로 던져 글로벌 핸들러에서 처리
            throw e;
        } catch (IllegalArgumentException e) {
            // 기타 예외도 던짐
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }
    // Request의 Header에서 token 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }
}
