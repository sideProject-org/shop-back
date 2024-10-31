package toy.shop.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDTO {

    private String accessToken;
    private String refreshToken;
}
