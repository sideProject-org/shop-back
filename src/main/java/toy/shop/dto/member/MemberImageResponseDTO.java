package toy.shop.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberImageResponseDTO {

    private String originalName;
    private String savedPath;
}
