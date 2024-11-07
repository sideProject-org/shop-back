package toy.shop.domain.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0L")
    private long viewCnt = 1L;

    @OneToMany(mappedBy = "notice")
    private List<NoticeImage> imagePaths = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private char deleteType = 'N';

    @Builder
    public Notice(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }
}
