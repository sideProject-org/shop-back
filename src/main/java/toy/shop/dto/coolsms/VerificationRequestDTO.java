package toy.shop.dto.coolsms;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "인증번호 확인에 필요한 요청 정보", requiredProperties = {"phoneNumber", "verificationCode"})
public class VerificationRequestDTO {

    @Schema(description = "휴대번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String phoneNumber;

    @Schema(description = "인증번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String verificationCode;
}
