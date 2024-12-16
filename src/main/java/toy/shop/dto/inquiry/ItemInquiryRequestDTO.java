package toy.shop.dto.inquiry;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "상품 문의에 필요한 요청 정보", requiredProperties = {"itemId", "title", "content"})
public class ItemInquiryRequestDTO {

    @Schema(description = "상품 ID")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long itemId;

    @Schema(description = "상품 문의 제목")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String title;

    @Schema(description = "상품 문의 내용")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String content;
}
