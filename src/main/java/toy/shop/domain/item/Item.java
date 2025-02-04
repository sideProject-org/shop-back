package toy.shop.domain.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.member.Member;

@Entity
@Getter
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int sale;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private char deleteType = 'N';

    @Builder
    public Item(String name, String content, int price, int sale, int quantity, String imagePath, Member member) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.sale = sale;
        this.quantity = quantity;
        this.imagePath = imagePath;
        this.member = member;
    }

    public void updateItem(String name, String content, int price, int sale, int quantity) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.sale = sale;
        this.quantity = quantity;
    }

    public void updateImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean updateQuantity(int quantity) {
        if (this.quantity < quantity) {
            return false;
        } else {
            this.quantity -= quantity;
            return true;
        }
    }

    public void deleteItem() {
        this.deleteType = 'Y';
    }

    public void recoverItem() {
        this.deleteType = 'N';
    }
}
