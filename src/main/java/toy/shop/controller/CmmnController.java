package toy.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.NoticeDetailResponseDTO;
import toy.shop.dto.admin.notice.NoticeListResponseDTO;
import toy.shop.dto.jwt.JwtReissueDTO;
import toy.shop.dto.jwt.JwtResponseDTO;
import toy.shop.dto.member.LoginRequestDTO;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.service.admin.notice.NoticeService;
import toy.shop.service.member.MemberService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cmmn")
public class CmmnController implements CmmnControllerDocs {

    private final MemberService memberService;
    private final NoticeService noticeService;

    /* ============================================= auth 부문 ============================================= */
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
    /* ============================================= auth 부문 ============================================= */


    /* ============================================= 공지사항 부문 ============================================= */
    @GetMapping("/notices")
    public ResponseEntity<Response<?>> noticeList(@PageableDefault(size = 10) Pageable pageable) {
        Page<NoticeListResponseDTO> result = noticeService.noticeList(pageable);

        return buildResponse(HttpStatus.OK, "공지사항 목록 조회 성공", result);
    }

    @GetMapping("/notices/{noticeId}")
    public ResponseEntity<Response<?>> noticeDetail(@PathVariable("noticeId") Long noticeId) {
        NoticeDetailResponseDTO result = noticeService.noticeDetail(noticeId);

        return buildResponse(HttpStatus.OK, "공지사항 상세정보 조회 성공", result);
    }

    @GetMapping("/notices/{noticeId}/view-cnt")
    public ResponseEntity<Response<?>> addViewCnt(@PathVariable Long noticeId) {
        long viewCnt = noticeService.addNoticeViewCnt(noticeId);

        return buildResponse(HttpStatus.OK, "조회수 증가 성공", viewCnt);
    }
    /* ============================================= 공지사항 부문 ============================================= */
}
