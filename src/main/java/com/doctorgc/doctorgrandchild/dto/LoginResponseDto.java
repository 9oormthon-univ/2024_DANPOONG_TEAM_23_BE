package com.doctorgc.doctorgrandchild.dto;

import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String nickname;
    private String profileImage;
    private String email;

    private boolean ReigsteredBefore ;

    private String accessToken;




    public LoginResponseDto(Long id, String nickname, String profileImage, String email,boolean RegisteredBefore,String accessToken){
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
        this.ReigsteredBefore = RegisteredBefore;
        this.accessToken = accessToken;
    }

}
