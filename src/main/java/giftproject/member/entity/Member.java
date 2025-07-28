package giftproject.member.entity;

import giftproject.gift.entity.Product;
import giftproject.wishlist.entity.Wish;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "kakao_access_token")
    private String kakaoAccessToken;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    protected Member() {
    }

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password, String kakaoAccessToken, Long kakaoId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoId = kakaoId;
    }

    public Member(String email, String password) {
        this(null, email, password, null, null);
    }

    public Member(String email, String kakaoAccessToken, Long kakaoId) {
        this(null, email, null, kakaoAccessToken, kakaoId);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    public void setKakaoAccessToken(String kakaoAccessToken) {
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addWish(Wish wish) {
        this.wishes.add(wish);
        if (wish.getMember() != this) {
            wish.setMember(this);
        }
    }

    public void removeWish(Wish wish) {
        this.wishes.remove(wish);
        if (wish.getMember() == this) {
            wish.setMember(null);
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

    public Wish addOrUpdateWish(Product product, int maxWishlistProductCount) {
        Optional<Wish> existingWishOptional = wishes.stream()
                .filter(wish -> wish.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingWishOptional.isPresent()) {
            Wish existingWish = existingWishOptional.get();
            existingWish.incrementQuantity();
            return existingWish;
        } else {
            long distinctProductCount = this.wishes.stream()
                    .map(Wish::getProduct)
                    .distinct()
                    .count();
            if (distinctProductCount >= maxWishlistProductCount) {
                throw new IllegalArgumentException(
                        "상품을 최대 " + maxWishlistProductCount + "종까지 담을 수 있어요.");
            }
        }

        Wish newWish = new Wish(this, product, 1);
        this.addWish(newWish);
        return newWish;
    }
}
