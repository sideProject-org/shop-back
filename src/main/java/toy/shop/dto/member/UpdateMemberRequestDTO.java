package toy.shop.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원 정보 변경에 필요한 요청 정보", requiredProperties = {"userEmail", "nickName", "phoneNumber"})
public class UpdateMemberRequestDTO {

    private String nickName;
    private String phoneNumber;
}
