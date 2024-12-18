package toy.shop.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.review.ItemReview;

public interface ItemReviewRepository extends JpaRepository<ItemReview, Long> {
}
