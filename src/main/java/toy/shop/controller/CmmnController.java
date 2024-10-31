package toy.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.jwt.JwtReissueDTO;
import toy.shop.dto.member.LoginRequestDTO;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.dto.jwt.JwtResponseDTO;
import toy.shop.service.member.MemberService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cmmn")
public class CmmnController implements CmmnControllerDocs {

    private final MemberService memberService;

    @PostMapping("/joinMember")
    public ResponseEntity<Response<?>> joinMember(@RequestBody @Valid SignupRequestDTO parameter) {
        Long result = memberService.signup(parameter);

        return buildResponse(HttpStatus.OK, "회원가입 성공", result);
    }

    @PostMapping("/signIn")
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
}
