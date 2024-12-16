package toy.shop.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "찜 등록에 필요한 요청 정보", requiredProperties = {"itemId"})
public class WishSaveRequestDTO {

    @Schema(description = "상품 ID")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long itemId;
}
