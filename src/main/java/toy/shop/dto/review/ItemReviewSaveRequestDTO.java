package toy.shop.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.cmmn.valid.rate.RateValid;

import java.util.List;

@Data
@Schema(description = "상품 후기 등록에 필요한 요청 정보", requiredProperties = {"itemId", "title", "content", "rate"})
public class ItemReviewSaveRequestDTO {

    @Schema(description = "상품 ID")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long itemId;

    @Schema(description = "후기 제목")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String title;

    @Schema(description = "후기 내용")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String content;

    @RateValid
    @Schema(description = "후기 별점")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Double rate;

    private List<MultipartFile> itemReviewImages;
}
