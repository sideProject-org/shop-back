package toy.shop.repository.admin.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.notice.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
