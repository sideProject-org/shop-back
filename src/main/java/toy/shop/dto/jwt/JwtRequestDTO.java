package toy.shop.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "토큰 재발급 및 로그아웃에 필요한 요청 정보", requiredProperties = {"accessToken", "refreshToken"})
public class JwtRequestDTO {

    @Schema(description = "엑세스 토큰")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String accessToken;

    @Schema(description = "리프레쉬 토큰")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String refreshToken;
}
