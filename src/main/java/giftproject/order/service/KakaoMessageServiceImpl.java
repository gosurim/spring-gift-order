package giftproject.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import giftproject.order.entity.Order;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoMessageServiceImpl implements KakaoMessageService {

    private static final Logger log = LoggerFactory.getLogger(KakaoMessageServiceImpl.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private String memoUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    public KakaoMessageServiceImpl(RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean sendOrderCompletionMessageToMe(String kakaoAccessToken, Order order,
            String customMessage) {
        String messageText = createMessageText(order, customMessage);

        Map<String, Object> body = new HashMap<>();
        body.put("object_type", "text");
        body.put("text", messageText);
        body.put("link", new HashMap<String, String>() {{
            put("web_url", "http://localhost:8080");
            put("mobile_web_url", "http://localhost:8080");
        }});
        body.put("button_title", "주문 내역 확인");

        String templateObjectJson;
        try {
            templateObjectJson = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            log.error("메시지 템플릿 JSON 변환 실패: {}", e.getMessage());
            return false;
        }

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("template_object", templateObjectJson);

        try {
            Map<String, Object> kakaoResponse = restClient.post()
                    .uri(memoUrl)
                    .header("Authorization", "Bearer " + kakaoAccessToken)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (request, response) -> {
                                throw new RuntimeException("카카오 메시지 전송 실패");
                            })
                    .body(new HashMap<String, Object>().getClass());
            return true;
        } catch (Exception e) {
            log.error("카카오톡 메시지 전송 중 예외 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    private String createMessageText(Order order, String customMessage) {
        StringBuilder messageText = new StringBuilder();
        messageText.append("주문 완료!\n\n");
        messageText.append("주문 번호: ").append(order.getId()).append("\n");
        messageText.append("옵션 ID: ").append(order.getOptionId()).append("\n");
        messageText.append("수량: ").append(order.getQuantity()).append("\n");
        messageText.append("주문 일시: ").append(order.getOrderDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        if (customMessage != null && !customMessage.isEmpty()) {
            messageText.append("메시지: ").append(customMessage).append("\n");
        }
        return messageText.toString();
    }
}
