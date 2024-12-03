package toy.shop.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
@Schema(description = "상품 수정에 필요한 요청 정보", requiredProperties = {"name", "price", "sale", "quantity"})
public class ItemUpdateRequestDTO {

    @Schema(description = "상품명")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String name;

    @Schema(description = "상품 설명")
    private String content;

    @Schema(description = "상품 정가")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private int price;

    @Schema(description = "상품 할인율")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private int sale;

    @Schema(description = "상품 재고")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private int quantity;

    @Schema(description = "상품 설명 이미지")
    private Optional<MultipartFile> itemDetailImage = Optional.empty();

    @Schema(description = "상품 이미지들")
    private List<MultipartFile> itemImages = List.of();
}
