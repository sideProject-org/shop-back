package toy.shop.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.item.Item;
import toy.shop.domain.member.Member;

@Entity
@Getter
@Table(name = "item_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Double rate;

    @Builder
    public ItemReview(Member member, Item item, String title, String content, Double rate) {
        this.member = member;
        this.item = item;
        this.title = title;
        this.content = content;
        this.rate = rate;
    }

    public void updateReview(String title, String content, Double rate) {
        this.title = title;
        this.content = content;
        this.rate = rate;
    }
}
