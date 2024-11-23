package com.doctorgc.doctorgrandchild.controller;





import com.doctorgc.doctorgrandchild.config.auth.UserDetailsImpl;
import com.doctorgc.doctorgrandchild.config.jwt.JwtTokenProvider;
import com.doctorgc.doctorgrandchild.config.util.CookieUtils;
import com.doctorgc.doctorgrandchild.dto.KakaoUserInfoResponseDto;
import com.doctorgc.doctorgrandchild.dto.LoginResponseDto;
import com.doctorgc.doctorgrandchild.repository.MemberRepository;
import com.doctorgc.doctorgrandchild.service.KakaoService;
import com.doctorgc.doctorgrandchild.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final MemberRepository memberRepository;

    @GetMapping("/{code}")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@PathVariable String code,
             HttpServletResponse httpServletResponse){
        //프론트로부터 받은 code로 kakao에 요청해 accesstoken 받기
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        //kakao로부터 받은 accesstoken으로 유저정보 받기
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        //받은 유저정보로 자체 db를 보고 있는 회원이면 로그인, 없으면 회원가입 후 로그인
        LoginResponseDto memberLoginResponse = kakaoService.kakaoUserLogin(userInfo,httpServletResponse);

        //memberLoginResponse를 어떻게 담아 보낼것인가?


        return ResponseEntity.ok(memberLoginResponse);
    }

//    @GetMapping("/{code}")
//    public ResponseEntity<LoginResponseDto> regi(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        //
//        //프론트로부터 받은 code로 kakao에 요청해 accesstoken 받기
//        String accessToken = kakaoService.getAccessTokenFromKakao(code);
//        //kakao로부터 받은 accesstoken으로 유저정보 받기
//        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
//        //받은 유저정보로 자체 db를 보고 있는 회원이면 로그인, 없으면 회원가입 후 로그인
//        LoginResponseDto memberLoginResponse = kakaoService.kakaoUserLogin(userInfo,httpServletResponse);
//
//        //memberLoginResponse를 어떻게 담아 보낼것인가?
//
//
//        return ResponseEntity.ok(memberLoginResponse);
//    }

        @PostMapping("/delete")
        public ResponseEntity<?> deleteAndDeactivateMember(@AuthenticationPrincipal UserDetailsImpl userDetails,HttpServletRequest request){
            try {
                //사용자 이메일 추출
                if (userDetails == null) {
                    log.error("UserDetails is null. Authentication failed.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 없습니다.");
                }
                log.info("Authenticated user: {}", userDetails.getUsername());
                String email = userDetails.getUsername();

                //Authorization 헤더에서 토큰 추출
                String authorizationHeader = request.getHeader("Authorization");
                if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization 헤더가 유효하지 않습니다.");
                }

                String token = authorizationHeader.replace("Bearer ", "").trim();

                //카카오 unlink API 호출
                kakaoService.unlinkKakaoUser(token);

                //사용자 비활성화 처리
                memberRepository.deactivateMember(email);

                return ResponseEntity.ok("회원탈퇴가 성공적으로 처리되었습니다.");
            } catch (RuntimeException e) {
                // 일반적인 RuntimeException 처리
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 탈퇴 중 오류가 발생했습니다: " + e.getMessage());
            } catch (Exception e) {
                // 기타 모든 예외 처리
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
            }
        }




}
