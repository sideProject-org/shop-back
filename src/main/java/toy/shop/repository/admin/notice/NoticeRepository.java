package toy.shop.repository.admin.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import toy.shop.domain.notice.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select n from Notice n join fetch n.member")
    Page<Notice> findAllWithMember(Pageable pageable);
}
