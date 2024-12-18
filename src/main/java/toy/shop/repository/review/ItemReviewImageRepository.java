package toy.shop.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.review.ItemReviewImage;

public interface ItemReviewImageRepository extends JpaRepository<ItemReviewImage, Long> {
}
