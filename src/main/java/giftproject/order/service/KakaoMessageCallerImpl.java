package giftproject.order.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoMessageCallerImpl implements KakaoMessageCaller {

    private final RestClient restClient;

    public KakaoMessageCallerImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Override
    public <T> Map<String, Object> post(String url, String authorization,
            MultiValueMap<String, T> body) {
        return restClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + authorization)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            throw new RuntimeException("카카오 메시지 전송 실패");
                        })
                .body(new HashMap<String, Object>().getClass());
    }
}
