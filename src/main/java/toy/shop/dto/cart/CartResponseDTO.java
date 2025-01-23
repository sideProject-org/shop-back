package toy.shop.dto.cart;

import lombok.Builder;
import lombok.Data;
import toy.shop.dto.item.ItemListResponseDTO;

@Data
@Builder
public class CartResponseDTO {

    private Long cartId;
    private int quantity;
    private ItemListResponseDTO item;
}
