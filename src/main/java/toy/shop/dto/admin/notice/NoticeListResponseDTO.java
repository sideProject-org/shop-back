package toy.shop.dto.admin.notice;

import lombok.Builder;
import lombok.Data;
import toy.shop.dto.member.MemberDetailResponseDTO;

@Data
@Builder
public class NoticeListResponseDTO {

    private Long id;
    private String title;
    private String content;
    private Long viewCnt;
    private MemberDetailResponseDTO member;
}
