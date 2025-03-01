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
import toy.shop.dto.admin.notice.comment.NoticeCommentResponseDTO;
import toy.shop.dto.inquiry.ItemInquiryResponseDTO;
import toy.shop.dto.item.ItemDetailResponseDTO;
import toy.shop.dto.item.ItemListResponseDTO;
import toy.shop.service.admin.notice.NoticeCommentService;
import toy.shop.service.admin.notice.NoticeService;
import toy.shop.service.inquiry.ItemInquiryCommentService;
import toy.shop.service.inquiry.ItemInquiryService;
import toy.shop.service.item.ItemService;

import java.util.List;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/global")
public class GlobalController implements GlobalControllerDocs {

    private final NoticeService noticeService;
    private final NoticeCommentService noticeCommentService;
    private final ItemService itemService;
    private final ItemInquiryService itemInquiryService;
    private final ItemInquiryCommentService itemInquiryCommentService;

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

    @GetMapping("/notices/{noticeId}/notice_comments")
    public ResponseEntity<Response<?>> noticeCommentList(@PathVariable Long noticeId) {
        List<NoticeCommentResponseDTO> result = noticeCommentService.noticeCommentList(noticeId);

        return buildResponse(HttpStatus.OK, "공지사항 댓글 목록 조회 성공", result);
    }

    @GetMapping("/items")
    public ResponseEntity<Response<?>> itemList(@PageableDefault(size = 10) Pageable pageable) {
        Page<ItemListResponseDTO> result = itemService.itemList(pageable);

        return buildResponse(HttpStatus.OK, "상품 목록 조회 성공", result);
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<Response<?>> itemDetail(@PathVariable("itemId") Long itemId) {
        ItemDetailResponseDTO result = itemService.itemDetail(itemId);

        return buildResponse(HttpStatus.OK, "상품 상세정보 조회 성공", result);
    }

    @GetMapping("/items/itemInquiries/{itemId}")
    public ResponseEntity<Response<?>> itemInquiryList(@PathVariable("itemId") Long itemId, @PageableDefault(size = 10) Pageable pageable) {
        Page<ItemInquiryResponseDTO> result = itemInquiryService.itemInquiryList(itemId, pageable);

        return buildResponse(HttpStatus.OK, "상품 문의 목록 조회 성공", result);
    }
}
