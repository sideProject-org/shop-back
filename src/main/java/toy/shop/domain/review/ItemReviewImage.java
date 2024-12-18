package toy.shop.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "item_review_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_review_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_review_id")
    private ItemReview itemReview;

    @Column(nullable = false)
    private String imagePath;
}
