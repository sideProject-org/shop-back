package toy.shop.dto.admin.notice.image;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "공지사항 임시 이미지 삭제에 필요한 요청 정보", requiredProperties = {"imagePath"})
public class NoticeTmpImageDeleteRequestDTO {

    @Schema(description = "이미지 경로")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String imagePath;
}
