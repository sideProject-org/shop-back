package toy.shop.coolsms;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import toy.shop.dto.coolsms.SMSRequestDTO;
import toy.shop.dto.coolsms.VerificationRequestDTO;

@Tag(name = "휴대폰 인증 API", description = "휴대폰 인증 로직에 관한 API")
public interface SMSControllerDocs {

    @Operation(summary = "메세지 전송", description = "Request 정보를 통해 메세지 전송")
    SingleMessageSentResponse sendOne(SMSRequestDTO parameter);

    @Operation(summary = "인증번호 확인", description = "Request 정보를 통해 인증번호 확인")
    boolean verifyCode(VerificationRequestDTO parameter);
}
