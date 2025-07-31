package giftproject.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import giftproject.order.entity.Order;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class KakaoMessageServiceImpl implements KakaoMessageService {

    private static final Logger log = LoggerFactory.getLogger(KakaoMessageServiceImpl.class);

    private final KakaoMessageCaller kakaoMessageCaller;
    private final ObjectMapper objectMapper;
    private final String memoUrl;

    public KakaoMessageServiceImpl(KakaoMessageCaller kakaoMessageCaller,
            ObjectMapper objectMapper, @Value("${kakao.send-memo-url}") String memoUrl) {
        this.kakaoMessageCaller = kakaoMessageCaller;
        this.objectMapper = objectMapper;
        this.memoUrl = memoUrl;
    }

    @Override
    public boolean sendOrderCompletionMessageToMe(String kakaoAccessToken, Order order,
            String customMessage) {
        try {
            String templateObjectJson = createTemplateObject(order, customMessage);
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("template_object", templateObjectJson);

            kakaoMessageCaller.post(memoUrl, kakaoAccessToken, requestBody);

            return true;
        } catch (Exception e) {
            log.error("메시지 템플릿 JSON 변환 실패: {}", e.getMessage());
            return false;
        }
    }

    private String createTemplateObject(Order order, String customMessage)
            throws JsonProcessingException {
        String messageText = createMessageText(order, customMessage);

        Map<String, Object> body = new HashMap<>();
        body.put("object_type", "text");
        body.put("text", messageText);
        body.put("link", new HashMap<String, String>() {{
            put("web_url", "http://localhost:8080");
            put("mobile_web_url", "http://localhost:8080");
        }});
        body.put("button_title", "주문 내역 확인");

        return objectMapper.writeValueAsString(body);
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
