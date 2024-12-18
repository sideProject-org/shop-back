package toy.shop.dto.inquiry;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemInquiryCommentResponseDTO {

    private Long itemInquiryCommentId;
    private String content;
    private String nickname;
    private String email;
    private LocalDateTime createdAt;
}
