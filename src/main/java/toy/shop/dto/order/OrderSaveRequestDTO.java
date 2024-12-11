package toy.shop.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "주문에 필요한 요청 정보", requiredProperties = {"addressId", "orderNumber", "paymentMethod", "totalPrice", "orderDetails"})
public class OrderSaveRequestDTO {

    @Schema(description = "배송지 ID")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long addressId;

    @Schema(description = "주문 번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String orderNumber;

    @Schema(description = "결제방식")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String paymentMethod;

    @Schema(description = "총 금액")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private int totalPrice;

    @Schema(description = "주문 상세")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private List<OrderDetailsSaveRequestDTO> orderDetails;
}
