package toy.shop.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "장바구니 수량 변경에 필요한 요청 정보", requiredProperties = {"quantity"})
public class CartUpdateRequestDTO {

    @Schema(description = "상품 수량")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private int quantity;
}
