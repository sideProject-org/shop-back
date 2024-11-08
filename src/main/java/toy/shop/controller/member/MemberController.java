package toy.shop.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.member.PasswordResetRequestDTO;
import toy.shop.dto.member.PasswordRestResponseDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.member.MemberService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        memberService.logout(requestAccessToken);

        return buildResponse(HttpStatus.OK, "로그아웃 성공", null);
    }

    @GetMapping("/password-reset-email")
    public ResponseEntity<Response<?>> sendResetPasswordEmail(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        PasswordRestResponseDTO result = memberService.sendResetEmail(userDetails);

        return buildResponse(HttpStatus.OK, "이메일 전송 성공", result);
    }

    @PutMapping("/password")
    public ResponseEntity<Response<?>> resetPassword(@RequestBody @Valid PasswordResetRequestDTO parameter) {
        boolean result = memberService.resetPassword(parameter);

        return buildResponse(HttpStatus.OK, "비밀번호 변경 성공", result);
    }
}
