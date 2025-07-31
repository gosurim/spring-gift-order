package giftproject.order.entity;

import giftproject.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(length = 500)
    private String message;

    protected Order() {
    }

    public Order(Member member, Long optionId, int quantity, LocalDateTime orderDateTime,
            String message) {
        this.member = member;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = LocalDateTime.now();
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
