package toy.shop.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordRestResponseDTO {

    private String userEmail;
    private String token;
}
