package toy.shop.dto.oauth;

import lombok.Builder;
import lombok.Data;
import toy.shop.domain.Role;

@Data
@Builder
public class UserDTO {

    private String name;
    private String socialName;
    private String email;
    private String profileImage;
    private Role role;
}
