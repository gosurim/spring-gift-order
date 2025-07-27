package giftproject.auth.kakao;

import java.util.HashMap;
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
    public ResponseEntity<Map<String, Object>> getKakaoAccessToken(
            @RequestParam("code") String authorizaionCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            String accessToken = kakaoAuthService.getKakaoAccessToken(authorizaionCode);
            System.out.println("액세스 토큰 발급 성공: " + accessToken);

            Map<String, Object> userInfo = kakaoAuthService.getKakaoUserInfo(accessToken);
            System.out.println("사용자 정보: " + userInfo);

            response.put("status", "SUCCESS");
            response.put("message", "사용자 정보 획득 성공");
            response.put("accessToken", accessToken);
            response.put("userInfo", userInfo);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "카카오 로그인 처리 중 오류 발생: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
