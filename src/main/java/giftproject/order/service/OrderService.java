package giftproject.order.service;

import giftproject.member.entity.Member;
import giftproject.member.repository.MemberRepository;
import giftproject.option.entity.Option;
import giftproject.option.repository.OptionRepository;
import giftproject.order.dto.OrderRequestDto;
import giftproject.order.dto.OrderResponseDto;
import giftproject.order.entity.Order;
import giftproject.order.repository.OrderRepository;
import giftproject.wishlist.repository.WishRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(KakaoMessageServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final KakaoMessageServiceImpl kakaoMessageService;

    public OrderService(OrderRepository orderRepository,
            OptionRepository optionRepository,
            WishRepository wishRepository, MemberRepository memberRepository,
            KakaoMessageServiceImpl kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponseDto create(OrderRequestDto request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다: " + memberId));

        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new NoSuchElementException(
                        "해당 상품 옵션을 찾을 수 없습니다: " + request.optionId()));

        option.subtractQuantity(request.quantity());
        optionRepository.save(option);

        wishRepository.findByMemberIdAndProductId(memberId, request.optionId())
                .ifPresent(wishRepository::delete);

        Order newOrder = new Order(
                member,
                request.optionId(),
                request.quantity(),
                LocalDateTime.now(),
                request.message()
        );
        Order savedOrder = orderRepository.save(newOrder);

        String kakaoAccessToken = member.getKakaoAccessToken();
        boolean isSent = kakaoMessageService.sendOrderCompletionMessageToMe(
                kakaoAccessToken,
                savedOrder,
                request.message()
        );
        if (isSent) {
            log.info("카카오톡 주문 완료 메시지 전송 성공");
        } else {
            log.warn("카카오톡 주문 완료 메시지 전송 실패");
        }

        return OrderResponseDto.from(savedOrder);
    }
}
