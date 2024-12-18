package toy.shop.dto.inquiry;

import lombok.Data;

@Data
public class ItemInquiryCommentResponseDTO {

    private Long itemInquiryCommentId;
    private String content;
    private String nickname;
    private String email;
}
