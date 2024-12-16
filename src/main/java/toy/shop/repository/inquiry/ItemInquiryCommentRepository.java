package toy.shop.repository.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.inquiry.ItemInquiryComment;

public interface ItemInquiryCommentRepository extends JpaRepository<ItemInquiryComment, Long> {
}
