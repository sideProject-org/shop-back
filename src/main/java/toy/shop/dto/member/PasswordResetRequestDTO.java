package toy.shop.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "비밀번호 변경에 필요한 요청 정보", requiredProperties = {"userEmail", "password", "token"})
public class PasswordResetRequestDTO {

    @Schema(description = "회원 이메일")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String userEmail;

    @Schema(description = "변경할 비밀번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String password;

    @Schema(description = "이메일 전송 시 받은 Token값")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String token;
}
