package com.doctorgc.doctorgrandchild.config.auth;

import com.doctorgc.doctorgrandchild.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final String email;

//    public UserDetailsImpl(String email){
//        this.email = email;
//    }

    //jwt 전용 설정 시작
//    @Column(length = 100, nullable = false, unique = true)
//    private String keyCode; // 로그인 식별키

//    @ElementCollection(fetch = FetchType.EAGER) //roles 컬렉션
//    private List<String> roles = new ArrayList<>();

    @Override   //사용자의 권한 목록 리턴 (없으므로 빈 리스트 반환)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    @Override
    public String getUsername() { //email로 사용자 식별
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    //jwt 전용 설정 끝



}
