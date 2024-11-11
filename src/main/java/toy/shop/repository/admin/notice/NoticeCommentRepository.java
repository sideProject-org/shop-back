package toy.shop.repository.admin.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.notice.NoticeComments;

import java.util.List;

public interface NoticeCommentRepository extends JpaRepository<NoticeComments, Long> {

    /**
     * 주어진 공지사항 ID에 해당하는 댓글 목록을 조회합니다.
     *
     * @param noticeId 댓글을 조회할 공지사항의 ID
     * @return 공지사항 ID에 해당하는 댓글 목록을 포함하는 {@link List} 객체.
     *         댓글이 존재하지 않을 경우 빈 {@link List}가 반환됩니다.
     */
    List<NoticeComments> findByNoticeId(Long noticeId);
}
