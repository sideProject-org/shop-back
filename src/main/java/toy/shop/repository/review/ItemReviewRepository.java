package toy.shop.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.review.ItemReview;

public interface ItemReviewRepository extends JpaRepository<ItemReview, Long> {

    Page<ItemReview> findAllByItemId(Long itemId, Pageable pageable);
}
