package toy.shop.dto.admin.notice.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "공지사항 댓글을 수정에 필요한 요청 정보", requiredProperties = {"commentId", "comment"})
public class NoticeCommentUpdateRequestDTO {

    @Schema(description = "댓글 아이디")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long commentId;

    @Schema(description = "댓글 내용")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String comment;
}
