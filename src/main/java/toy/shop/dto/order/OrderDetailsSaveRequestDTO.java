package toy.shop.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "주문 상세에 필요한 요청 정보", requiredProperties = {"itemId", "itemPrice", "itemQuantity"})
public class OrderDetailsSaveRequestDTO {

    @Schema(description = "상품 ID")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long itemId;

    @Schema(description = "상품 금액")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private int itemPrice;

    @Schema(description = "상품 갯수")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private int itemQuantity;
}
