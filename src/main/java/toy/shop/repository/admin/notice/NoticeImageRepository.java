package toy.shop.repository.admin.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.notice.NoticeImage;

import java.util.List;

public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {

    /**
     * 지정된 공지사항 ID에 연관된 모든 NoticeImage 엔티티를 삭제합니다.
     *
     * 이 메서드는 데이터베이스에서 주어진 noticeId와 연관된 모든 공지사항
     * 이미지를 한 번에 삭제합니다. 동일한 공지사항에 여러 개의 이미지가
     * 연결되어 있는 경우, 이 메서드를 호출하면 모두 삭제됩니다.
     *
     * @param noticeId 삭제할 이미지가 연관된 공지사항의 ID (null이 아니어야 함)
     * @throws IllegalArgumentException noticeId가 null일 경우 발생
     */
    void deleteByNoticeId(Long noticeId);

    /**
     * 공지사항 ID에 해당하는 모든 NoticeImage 엔티티를 조회하는 메서드입니다.
     *
     * 이 메서드는 데이터베이스에서 주어진 공지사항 ID와 연관된 모든 이미지
     * 엔티티를 반환합니다. 공지사항에 여러 개의 이미지가 포함될 수 있으므로,
     * 결과는 List 형태로 반환됩니다.
     *
     * @param noticeId 조회할 공지사항의 ID (null이 아니어야 함)
     * @return 공지사항 ID에 해당하는 NoticeImage 엔티티 목록
     *         이미지가 없을 경우 빈 리스트를 반환합니다.
     * @throws IllegalArgumentException noticeId가 null인 경우 발생합니다.
     */
    List<NoticeImage> findByNoticeId(Long noticeId);
}
