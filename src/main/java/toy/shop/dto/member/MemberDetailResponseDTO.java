package toy.shop.dto.member;

import lombok.Builder;
import lombok.Data;
import toy.shop.domain.Role;

@Data
@Builder
public class MemberDetailResponseDTO {

    private Long id;
    private String email;
    private String nickName;
    private Role role;
}
