package toy.shop.dto.admin.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "공지사항 등록에 필요한 요청 정보", requiredProperties = {"memberId", "title", "content"})
public class SaveNoticeRequestDTO {

    @Schema(description = "사용자 ID")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long memberId;

    @Schema(description = "공지사항 제목")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String title;

    @Schema(description = "공지사항 내용")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String content;

    @Schema(description = "임시 이미지 주소")
    private List<String> tempImageUrls;
}
