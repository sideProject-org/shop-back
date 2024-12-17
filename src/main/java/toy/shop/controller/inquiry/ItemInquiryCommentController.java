package toy.shop.controller.inquiry;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.inquiry.ItemInquiryCommentSaveRequestDTO;
import toy.shop.dto.inquiry.ItemInquiryCommentUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.inquiry.ItemInquiryCommentService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itemInquiry/comments")
public class ItemInquiryCommentController implements ItemInquiryCommentControllerDocs {

    private final ItemInquiryCommentService itemInquiryCommentService;

    @PostMapping
    public ResponseEntity<Response<?>> answerItemInquiry(@RequestBody @Valid ItemInquiryCommentSaveRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemInquiryCommentService.registerComment(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "상품 문의 답변 성공", result);
    }

    @PutMapping("/{itemInquiryCommentId}")
    public ResponseEntity<Response<?>> updateItemInquiryComment(
            @PathVariable("itemInquiryCommentId") Long commentId,
            @RequestBody @Valid ItemInquiryCommentUpdateRequestDTO parameter,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemInquiryCommentService.updateComment(parameter, commentId, userDetails);

        return buildResponse(HttpStatus.OK, "상품 문의 답변 수정 성공", result);
    }

    @DeleteMapping("/{itemInquiryCommentId}")
    public ResponseEntity<Response<?>> deleteItemInquiryComment(@PathVariable("itemInquiryCommentId") Long commentId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        itemInquiryCommentService.deleteComment(commentId, userDetails);

        return buildResponse(HttpStatus.OK, "상품 문의 답변 삭제 성공", null);
    }
}
