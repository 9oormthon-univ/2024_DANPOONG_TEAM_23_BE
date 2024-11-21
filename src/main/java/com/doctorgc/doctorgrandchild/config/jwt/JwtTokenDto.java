package com.doctorgc.doctorgrandchild.config.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

}
