package com.doctorgc.doctorgrandchild.config.security;

import com.doctorgc.doctorgrandchild.config.jwt.AuthenticationValidationFilter;
import com.doctorgc.doctorgrandchild.config.jwt.JwtAuthFilter;
import com.doctorgc.doctorgrandchild.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationValidationFilter authenticationValidationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 사용 안함
                .sessionManagement(session -> session
                                                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize //인증이 필요하고 필요하지않은 url 구분
                                                            .requestMatchers("api/v1/members/{code}").permitAll()
                                                            .anyRequest().authenticated()) //나머지는 모두 권한 필요
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationValidationFilter,
                        UsernamePasswordAuthenticationFilter.class);
                // Http 요청에 대한 Jwt 유효성 선 검사


        return http.build();
    }


}
