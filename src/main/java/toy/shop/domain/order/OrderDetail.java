package toy.shop.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toy.shop.domain.item.Item;

@Entity
@Getter
@Table(name = "order_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @Builder
    public OrderDetail(Order order, Item item, int price, int quantity) {
        this.order = order;
        this.item = item;
        this.price = price;
        this.quantity = quantity;
    }
}
