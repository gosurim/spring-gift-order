package giftproject.auth.kakao;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoLoginController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping
    public ResponseEntity<Void> getAccessToken(
            @RequestParam("code") String authorizationCode) {
        String accessToken = kakaoAuthService.getKakaoAccessToken(authorizationCode);
        System.out.println("액세스 토큰 발급 성공: " + accessToken);
        Map<String, Object> userInfo = kakaoAuthService.getKakaoUserInfo(accessToken);
        System.out.println("사용자 정보: " + userInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
