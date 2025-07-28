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
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository,
            OptionRepository optionRepository,
            WishRepository wishRepository, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
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

        return OrderResponseDto.from(savedOrder);
    }
}
