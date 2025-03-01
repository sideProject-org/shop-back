package toy.shop.domain.inquiry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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
    @ColumnDefault("'0'")
    private char answerStatus = '0';      // 0: 미완료, 1: 답변완료

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Builder
    public ItemInquiry(Item item, Member member, String title, String content) {
        this.item = item;
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public void updateInquiry(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateAnswer(char answerStatus) {
        this.answerStatus = answerStatus;
    }
}
