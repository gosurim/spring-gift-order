package giftproject.order.service;

import java.util.Map;
import org.springframework.util.MultiValueMap;

public interface KakaoMessageCaller {

    <T> Map<String, Object> post(String url, String authorization, MultiValueMap<String, T> body);
}
