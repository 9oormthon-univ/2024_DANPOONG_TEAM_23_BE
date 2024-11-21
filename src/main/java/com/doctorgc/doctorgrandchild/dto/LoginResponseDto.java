package com.doctorgc.doctorgrandchild.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String nickname;
    private String profileImage;
    private String email;

    public LoginResponseDto(Long id, String nickname, String profileImage, String email){
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
    }

}
