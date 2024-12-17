package toy.shop.domain.inquiry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.member.Member;

@Entity
@Getter
@Table(name = "item_inquiry_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInquiryComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_inquiry_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_inquiry_id")
    private ItemInquiry itemInquiry;

    @Column(nullable = false)
    private String content;

    @Builder
    public ItemInquiryComment(Member member, ItemInquiry itemInquiry, String content) {
        this.member = member;
        this.itemInquiry = itemInquiry;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
