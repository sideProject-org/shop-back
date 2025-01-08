package toy.shop.dto.review;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemReviewResponseDTO {

    private Long id;
    private String title;
    private String content;
    private Double rate;
    private List<String> itemReviewImagePaths;
}
