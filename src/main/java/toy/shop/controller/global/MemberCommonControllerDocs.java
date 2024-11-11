package toy.shop.controller.global;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.comment.NoticeCommentRequestDTO;

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
    ResponseEntity<Response<?>> saveNoticeComment(Long noticeId, NoticeCommentRequestDTO parameter, Authentication authentication);
}
