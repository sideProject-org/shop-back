package toy.shop.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "로그인에 필요한 요청 정보", requiredProperties = {"email", "password"})
public class LoginRequestDTO {

    @Schema(description = "회원 이메일")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String email;

    @Schema(description = "회원 비밀번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String password;
}
