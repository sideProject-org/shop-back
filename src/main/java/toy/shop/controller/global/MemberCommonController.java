package toy.shop.controller.global;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.comment.NoticeCommentDeleteRequestDTO;
import toy.shop.dto.admin.notice.comment.NoticeCommentSaveRequestDTO;
import toy.shop.dto.admin.notice.comment.NoticeCommentUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.admin.notice.NoticeCommentService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/common")
public class MemberCommonController implements MemberCommonControllerDocs {

    private final NoticeCommentService noticeCommentService;

    @PostMapping("/notice-comment/{noticeId}")
    public ResponseEntity<Response<?>> saveNoticeComment(@PathVariable Long noticeId,
                                                         @RequestBody @Valid NoticeCommentSaveRequestDTO parameter,
                                                         Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Long result = noticeCommentService.saveNoticeComment(parameter, noticeId, userDetails);

        return buildResponse(HttpStatus.CREATED, "공지사항 댓글 작성 성공", result);
    }

    @PutMapping("/notice-comment/{noticeId}")
    public ResponseEntity<Response<?>> updateNoticeComment(@PathVariable Long noticeId,
                                                           @RequestBody @Valid NoticeCommentUpdateRequestDTO parameter,
                                                           Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Long result = noticeCommentService.updateNoticeComment(parameter, noticeId, userDetails);

        return buildResponse(HttpStatus.OK, "공지사항 댓글 수정 성공", result);
    }

    @DeleteMapping("/notice-comment/{noticeId}")
    public ResponseEntity<Response<?>> deleteNoticeComment(@PathVariable Long noticeId,
                                                           @RequestBody @Valid NoticeCommentDeleteRequestDTO parameter,
                                                           Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Long result = noticeCommentService.deleteNoticeComment(parameter, noticeId, userDetails);

        return buildResponse(HttpStatus.OK, "공지사항 댓글 삭제 성공", result);
    }
}
