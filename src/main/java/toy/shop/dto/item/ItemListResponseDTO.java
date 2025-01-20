package toy.shop.dto.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemListResponseDTO {

    private Long id;
    private String name;
    private int price;
    private int sale;
    private String itemImage;
}
