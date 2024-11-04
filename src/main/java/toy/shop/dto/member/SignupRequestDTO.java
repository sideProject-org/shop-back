package toy.shop.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toy.shop.domain.Role;

@Data
@NoArgsConstructor
@Schema(description = "회원가입에 필요한 요청 정보", requiredProperties = {"email", "password", "nickname", "gender", "role", "phone"})
public class SignupRequestDTO {

    @Schema(description = "회원 아이디")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String userId;

    @Schema(description = "회원 이메일")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String email;

    @Schema(description = "회원 비밀번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String password;

    @Schema(description = "회원 닉네임")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String nickname;

    @Schema(description = "회원 성별", example = "W or M")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    @Pattern(regexp = "W|M", message = "해당 값은 W 또는 M 이어야 합니다.")
    private String gender;

    @Schema(description = "회원 권한", example = "ROLE_USER or ROLE_COMPANY")
    @NotNull(message = "해당 값은 필수값 입니다.")
    private Role role;

    @Schema(description = "휴대번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String phone;

    @Builder
    public SignupRequestDTO(String userId, String email, String password, String nickname, String gender, Role role, String phone) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.role = role;
        this.phone = phone;
    }
}
