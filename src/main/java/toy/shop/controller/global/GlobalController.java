package toy.shop.controller.global;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.NoticeDetailResponseDTO;
import toy.shop.dto.admin.notice.NoticeListResponseDTO;
import toy.shop.service.admin.notice.NoticeService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/global")
public class GlobalController implements GlobalControllerDocs {


    private final NoticeService noticeService;

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
}
