package giftproject.order.controller;

import giftproject.member.annotation.LoginMember;
import giftproject.member.entity.Member;
import giftproject.order.dto.OrderRequestDto;
import giftproject.order.dto.OrderResponseDto;
import giftproject.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(
            @Valid @RequestBody OrderRequestDto requestDto,
            @LoginMember Member member) {
        OrderResponseDto response = orderService.create(requestDto, member.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
