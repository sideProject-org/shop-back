package toy.shop.controller.sms;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.dto.coolsms.SMSRequestDTO;
import toy.shop.dto.coolsms.VerificationRequestDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/api/verify")
public class SmsController implements SmsControllerDocs {

    private DefaultMessageService defaultMessageService;
    private Map<String, String> verificationCodes = new HashMap<>();

    @Value("${coolsms.key}")
    private String key;

    @Value("${coolsms.secret}")
    private String secret;

    @Value("${coolsms.number}")
    private String number;

    @PostConstruct
    public void init() {
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(key, secret, "https://api.coolsms.co.kr");
    }

    @PostMapping("/sms-send-one")
    public SingleMessageSentResponse smsSendOne(@RequestBody @Valid SMSRequestDTO parameter) {
        String phoneNumber = parameter.getPhoneNumber();
        String verificationCode = generateVerificationCode();
        verificationCodes.put(phoneNumber, verificationCode); // 전화번호와 인증번호 매핑

        Message message = new Message();
        message.setFrom(number);
        message.setTo(phoneNumber);
        message.setText("[쇼핑몰] 인증번호는 " + verificationCode + "입니다.");

        SingleMessageSentResponse response = this.defaultMessageService.sendOne(new SingleMessageSendingRequest(message));

        return response;
    }

    @PostMapping("/sms-verify-code")
    public boolean smsVerifyCode(@RequestBody @Valid VerificationRequestDTO parameter) {
        String sentCode = verificationCodes.get(parameter.getPhoneNumber());
        return parameter.getVerificationCode().equals(sentCode);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999 사이의 랜덤 6자리 숫자 생성
        return String.valueOf(code);
    }
}
