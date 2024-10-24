package toy.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.dto.Response;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.service.member.MemberService;

import static toy.shop.controller.ResponseBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cmmn")
public class CmmnController implements CmmnDocs {

    private final MemberService memberService;

    @PostMapping("/joinMember")
    public ResponseEntity<Response<?>> joinMember(SignupRequestDTO parameter) {
        Long result = memberService.signup(parameter);

        return buildResponse(HttpStatus.OK, "회원가입 성공", result);
    }
}
