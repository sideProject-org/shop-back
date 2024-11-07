package toy.shop.repository.admin.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.notice.NoticeImage;

public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {
}
