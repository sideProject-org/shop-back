package toy.shop.dto.admin.notice.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "공지사항 댓글을 삭제에 필요한 요청 정보", requiredProperties = {"commentId"})
public class NoticeCommentDeleteRequestDTO {

    @Schema(description = "댓글 아이디")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Long commentId;
}
