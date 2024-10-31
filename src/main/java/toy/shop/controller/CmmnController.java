package toy.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.dto.Response;
import toy.shop.dto.member.LoginRequestDTO;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.jwt.JwtDTO;
import toy.shop.service.member.MemberService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cmmn")
public class CmmnController implements CmmnDocs {

    private final MemberService memberService;
    private final long COOKIE_EXPIRATION = 7776000;

    @PostMapping("/joinMember")
    public ResponseEntity<Response<?>> joinMember(@RequestBody @Valid SignupRequestDTO parameter) {
        Long result = memberService.signup(parameter);

        return buildResponse(HttpStatus.OK, "회원가입 성공", result);
    }

    @PostMapping("/signIn")
    public  ResponseEntity<Response<?>> signIn(@RequestBody @Valid LoginRequestDTO parameter) {
        // User 등록 및 Refresh Token 저장
        JwtDTO result = memberService.login(parameter);

        return buildResponse(HttpStatus.OK, "로그인 성공", result);

//        Response<?> responseResult = Response.builder()
//                .status(HttpStatus.OK.value())
//                .message("로그인 성공")
//                .data(result)
//                .build();
//
//        ResponseCookie cookie = ResponseCookie.from("refresh-token", result.getRefreshToken())
//                .maxAge(COOKIE_EXPIRATION)
//                .httpOnly(true)
//                .secure(true)
//                .build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
//
//        return new ResponseEntity<>(responseResult, headers, status);
    }
}
