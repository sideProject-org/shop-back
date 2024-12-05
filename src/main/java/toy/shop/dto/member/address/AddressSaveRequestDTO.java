package toy.shop.dto.member.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "배송지 등록에 필요한 요청 정보", requiredProperties = {"name", "addr", "addrDetail", "phone", "zipCode"})
public class AddressSaveRequestDTO {

    @Schema(description = "받는 분 이름")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String name;

    @Schema(description = "주소")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String addr;

    @Schema(description = "배송지 별명")
    private String addrNickName;

    @Schema(description = "상세주소")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String addrDetail;

    @Schema(description = "받는 분 연락처")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String phone;

    @Schema(description = "우편번호")
    @NotBlank(message = "해당 값은 필수값 입니다.")
    private String zipCode;

    @Schema(description = "요청 사항")
    private String request;
}
