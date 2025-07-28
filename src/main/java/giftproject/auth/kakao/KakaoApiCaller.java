package giftproject.auth.kakao;

import java.util.Map;

public interface KakaoApiCaller {

    String getKakaoAccessToken(String authrizationCode);

    Map<String, Object> getKakaoUserInfo(String accessToken);
}
