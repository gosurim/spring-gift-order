package giftproject.auth.kakao;

import giftproject.member.entity.Member;
import giftproject.member.service.MemberService;
import giftproject.member.util.JwtTokenProvider;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private static final Logger log = LoggerFactory.getLogger(KakaoLoginController.class);

    private final KakaoAuthService kakaoAuthService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoLoginController(KakaoAuthService kakaoAuthService, MemberService memberService,
            JwtTokenProvider jwtTokenProvider) {
        this.kakaoAuthService = kakaoAuthService;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getKakaoAccessToken(
            @RequestParam("code") String authorizaionCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            String accessToken = kakaoAuthService.getKakaoAccessToken(authorizaionCode);
            log.info("액세스 토큰 발급 성공: {}", accessToken);

            Map<String, Object> userInfo = kakaoAuthService.getKakaoUserInfo(accessToken);
            Long kakaoId = (Long) userInfo.get("id");
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
            log.info("사용자 정보: {}", userInfo);
            String email = null;
            Member member = memberService.saveOrUpdateKakaoAccessTokenForMember(kakaoId, email,
                    accessToken);

            String ourServiceJwt = jwtTokenProvider.generateToken(member.getId());

            response.put("status", "SUCCESS");
            response.put("message", "카카오 로그인 및 회원 정보 연동 성공");
            response.put("ourServiceJwt", ourServiceJwt);
            response.put("memberId", member.getId());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "카카오 로그인 처리 중 오류 발생: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
