package giftproject.auth.kakao;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService {

    private final KakaoApiCaller kakaoApiCaller;

    public KakaoAuthService(KakaoApiCaller kakaoApiCaller) {
        this.kakaoApiCaller = kakaoApiCaller;
    }

    public String getKakaoAccessToken(String authorizationCode) {
        return kakaoApiCaller.getKakaoAccessToken(authorizationCode);
    }

    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        return kakaoApiCaller.getKakaoUserInfo(accessToken);
    }
}
