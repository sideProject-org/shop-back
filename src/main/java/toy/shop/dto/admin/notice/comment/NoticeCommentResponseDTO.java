package toy.shop.dto.admin.notice.comment;

import lombok.Builder;
import lombok.Data;
import toy.shop.dto.member.MemberDetailResponseDTO;

@Data
@Builder
public class NoticeCommentResponseDTO {

    private Long id;
    private MemberDetailResponseDTO member;
    private String comment;
}
