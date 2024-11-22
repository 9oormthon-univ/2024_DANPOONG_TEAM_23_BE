package com.doctorgc.doctorgrandchild.config.jwt;

import static org.hibernate.query.sqm.tree.SqmNode.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//@Slf4j
//@Component
//public class AuthenticationValidationFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
//            log.error("인증 정보가 유효하지 않습니다.");
//            throw new IllegalArgumentException("인증 정보가 유효하지 않습니다.");
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}

@Slf4j
@Component
public class AuthenticationValidationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationValidationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String token = authorization.substring(7, authorization.length());



        if (jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("JWT 토큰이 유효하지 않습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
