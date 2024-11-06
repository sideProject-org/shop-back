package toy.shop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final RedisService redisService;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${props.reset-password-url}")
    private String resetPwUrl;

    /**
     * 비밀번호 재설정 이메일을 생성하여 발송하는 메서드입니다.
     * 비밀번호 재설정에 필요한 토큰을 생성하고, 해당 토큰이 포함된 이메일을 지정된 사용자에게 보냅니다.
     *
     * @param userEmail 비밀번호 재설정 이메일을 받을 사용자의 이메일 주소
     * @return 비밀번호 재설정에 사용되는 고유 토큰 (UUID)
     */
    @Transactional
    public String generateResetEmail(String userEmail) {
        String uuid = redisService.generatePwResetToken(userEmail);

        String title = "[쇼핑몰] 비밀번호 재설정 링크입니다.";
        String content = "아래 링크에 접속하여 비밀번호를 재설정 해주세요.<br><br>"
                + "<a href=\"" + resetPwUrl + "/" + uuid + "\">"
                + resetPwUrl + "/" + uuid + "</a>"
                + "<br><br>해당 링크는 24시간 동안 유효하며, 1회 변경 가능합니다.<br>";

        sendMail(userEmail, title, content);

        return uuid;
    }

    /**
     * 이메일을 발송하는 메서드입니다. 지정된 제목과 내용을 사용하여 사용자에게 이메일을 전송합니다.
     *
     * @param userEmail 수신자 이메일 주소
     * @param title 이메일 제목
     * @param content 이메일 본문 (HTML 형식 지원)
     * @throws RuntimeException 이메일 발송 중 문제가 발생할 경우 발생합니다.
     */
    public void sendMail(String userEmail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(new InternetAddress(fromEmail, "쇼핑몰"));
            helper.setTo(userEmail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException exception) {
            throw new RuntimeException(exception);
        }
    }

}
