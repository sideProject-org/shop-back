package toy.shop.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.member.Address;
import toy.shop.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private char status = '0';

    @Column
    private String reason;

    @Column
    private String paymentMethod;

    @Column(nullable = false)
    private int totalPrice;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Builder
    public Order (Member member, Address address, String orderNumber, String paymentMethod, int totalPrice) {
        this.member = member;
        this.address = address;
        this.orderNumber = orderNumber;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }
}
