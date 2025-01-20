package toy.shop.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Schema(description = "상품 등록에 필요한 요청 정보", requiredProperties = {"name", "price", "sale", "quantity", "itemDescriptionImage", "itemImages"})
public class ItemSaveRequestDTO {

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
    @NotNull(message = "해당 값은 필수값 입니다.")
    private MultipartFile itemDescriptionImage;

    @Schema(description = "상품 이미지들")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private List<MultipartFile> itemImages;
}
