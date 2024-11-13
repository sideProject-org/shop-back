package toy.shop.controller.global;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.comment.NoticeCommentDeleteRequestDTO;
import toy.shop.dto.admin.notice.comment.NoticeCommentSaveRequestDTO;
import toy.shop.dto.admin.notice.comment.NoticeCommentUpdateRequestDTO;

@Tag(name = "회원 공통 API", description = "회원들의 공통 기능들에 대한 API")
public interface MemberCommonControllerDocs {

    @Operation(summary = "공지사항 댓글 작성", description = "Request 정보와 공지사항 ID를 통해 댓글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "공지사항 댓글 작성 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "공지사항 댓글 작성 성공",
                        "data": "댓글 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "공지사항 댓글 작성 실패 - 유효하지 않은 공지사항 ID", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "공지사항이 존재하지 않습니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "공지사항 댓글 작성 실패 - 유효하지 않은 사용자", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자 아이디입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> saveNoticeComment(Long noticeId, NoticeCommentSaveRequestDTO parameter, Authentication authentication);

    @Operation(summary = "공지사항 댓글 수정", description = "Request 정보와 공지사항 ID를 통해 댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 댓글 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "공지사항 댓글 수정 성공",
                        "data": "댓글 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "공지사항 댓글 수정 실패 - 존재하지 않는 댓글", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 댓글입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "공지사항 댓글 수정 실패 - 유효하지 않은 사용자", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "댓글 작성자가 아닙니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateNoticeComment(Long noticeId,NoticeCommentUpdateRequestDTO parameter, Authentication authentication);

    @Operation(summary = "공지사항 댓글 삭제", description = "Request 정보와 공지사항 ID를 통해 댓글 삭제 <관리자는 모두 삭제 가능>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 댓글 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "공지사항 댓글 삭제 성공",
                        "data": "댓글 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "공지사항 댓글 삭제 실패 - 존재하지 않는 댓글", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 댓글입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "공지사항 댓글 삭제 실패 - 유효하지 않은 사용자", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "댓글 작성자가 아닙니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteNoticeComment(Long noticeId, NoticeCommentDeleteRequestDTO parameter, Authentication authentication);
}
