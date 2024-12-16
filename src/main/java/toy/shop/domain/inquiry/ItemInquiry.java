package toy.shop.domain.inquiry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.item.Item;
import toy.shop.domain.member.Member;

@Entity
@Getter
@Table(name = "item_inquiry")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
}
