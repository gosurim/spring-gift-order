package giftproject.auth.kakao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/kakao")
public class KakaoLoginController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoLoginController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping
    public ResponseEntity<Void> getKakaoAccessToken(@RequestParam("code") String authorizaionCode) {
        try {
            String accessToken = kakaoAuthService.getKakaoAccessToken(authorizaionCode);
            System.out.println("액세스 토큰 발급 성공: " + accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("액세스 토큰 발급 실패: " + e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
