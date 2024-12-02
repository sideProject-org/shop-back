package toy.shop.dto.item;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemDetailResponseDTO {

    private Long id;
    private String name;
    private int price;
    private int sale;
    private String content;
    private String imageDetail;
    private List<String> imageList;
}
