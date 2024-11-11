package toy.shop.domain.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.member.Member;

@Entity
@Getter
@Table(name = "notice_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeComments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_comments_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String comment;

    @Builder
    public NoticeComments(Notice notice, Member member, String comment) {
        this.notice = notice;
        this.member = member;
        this.comment = comment;
    }
}
