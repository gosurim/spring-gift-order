package giftproject.auth.kakao;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestClient kakaoAuthRestClient;
    private final RestClient kakaoApiRestClient;

    public KakaoAuthService(RestClient.Builder restClientBuilder) {
        this.kakaoAuthRestClient = restClientBuilder.baseUrl("https://kauth.kakao.com").build();
        this.kakaoApiRestClient = restClientBuilder.baseUrl("https://kapi.kakao.com").build();
    }

    public String getKakaoAccessToken(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        Map<String, Object> responseBody = kakaoAuthRestClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return responseBody.get("access_token").toString();
    }

    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> responseBody = kakaoApiRestClient.get()
                .uri("/v2/user/me")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            throw new RuntimeException("카카오 토큰 요청 실패");
                        })
                .body(Map.class);

        return responseBody;
    }
}
