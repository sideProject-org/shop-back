package toy.shop.repository.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.inquiry.ItemInquiryComment;

import java.util.Optional;

public interface ItemInquiryCommentRepository extends JpaRepository<ItemInquiryComment, Long> {

    Optional<ItemInquiryComment> findByItemInquiry_Id(Long itemInquiryId);
}
