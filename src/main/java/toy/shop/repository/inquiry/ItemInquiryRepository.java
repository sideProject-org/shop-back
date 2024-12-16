package toy.shop.repository.inquiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.inquiry.ItemInquiry;

public interface ItemInquiryRepository extends JpaRepository<ItemInquiry, Long> {

    Page<ItemInquiry> findAllByItemId(Long itemId, Pageable pageable);
}
