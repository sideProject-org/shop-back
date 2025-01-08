package toy.shop.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.review.ItemReviewImage;

import java.util.List;

public interface ItemReviewImageRepository extends JpaRepository<ItemReviewImage, Long> {

    List<String> findImagePathByItemReview_id(Long itemReviewId);
    void deleteAllByItemReview_id(Long itemReviewId);
}
