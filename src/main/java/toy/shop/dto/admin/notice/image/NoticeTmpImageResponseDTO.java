package toy.shop.dto.admin.notice.image;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeTmpImageResponseDTO {

    private String originalName;
    private String savedPath;
}
