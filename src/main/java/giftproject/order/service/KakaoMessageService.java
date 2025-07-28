package giftproject.order.service;

import giftproject.order.entity.Order;

public interface KakaoMessageService {

    boolean sendOrderCompletionMessageToMe(String KaKaoAccessToken, Order order, String message);
}
