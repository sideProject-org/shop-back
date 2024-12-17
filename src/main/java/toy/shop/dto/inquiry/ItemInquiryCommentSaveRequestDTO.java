package toy.shop.dto.inquiry;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "상품 문의 답변에 필요한 요청 정보", requiredProperties = {"itemInquiryId", "content"})
public class ItemInquiryCommentSaveRequestDTO {

    @Schema(description = "상품 문의 ID")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long itemInquiryId;

    @Schema(description = "답변 내용")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String content;
}
