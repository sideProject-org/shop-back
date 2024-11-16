package toy.shop.controller.global;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.dto.Response;
import toy.shop.dto.jwt.JwtReissueDTO;
import toy.shop.dto.jwt.JwtResponseDTO;
import toy.shop.dto.member.LoginRequestDTO;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.service.member.MemberService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<Response<?>> joinMember(@RequestBody @Valid SignupRequestDTO parameter) {
        Long result = memberService.signup(parameter);

        return buildResponse(HttpStatus.CREATED, "회원가입 성공", result);
    }

    @PostMapping("/sign-in")
    public  ResponseEntity<Response<?>> signIn(@RequestBody @Valid LoginRequestDTO parameter) {
        JwtResponseDTO result = memberService.login(parameter);

        return buildResponse(HttpStatus.OK, "로그인 성공", result);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String requestAccessToken, @RequestBody @Valid JwtReissueDTO parameter) {
        JwtResponseDTO result = memberService.reissue(requestAccessToken, parameter.getRefreshToken());

        if (result != null) {
            return buildResponse(HttpStatus.OK, "토큰 재발급 성공", result);
        } else {
            return buildResponse(HttpStatus.UNAUTHORIZED, "재로그인 하세요", null);
        }
    }

    @PostMapping("/resend-token")
    public ResponseEntity<?> resendToken(HttpServletRequest request) {
        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 원하는 쿠키를 검색
            for (Cookie cookie : cookies) {
                if ("Authorization".equalsIgnoreCase(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("RefreshToken".equalsIgnoreCase(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (accessToken == null || refreshToken == null) {
            throw new NotFoundException("Cookie가 존재하지 않습니다.");
        }

        JwtResponseDTO result = JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return buildResponse(HttpStatus.OK, "토큰 재전송 성공", result);
    }
}
