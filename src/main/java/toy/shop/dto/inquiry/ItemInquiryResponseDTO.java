package toy.shop.dto.inquiry;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemInquiryResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String answerStatus;
    private String nickname;
    private LocalDateTime createdAt;
    private ItemInquiryCommentResponseDTO itemInquiryComment;
}
