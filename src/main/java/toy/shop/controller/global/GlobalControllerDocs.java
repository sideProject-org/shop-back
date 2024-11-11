package toy.shop.controller.global;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import toy.shop.dto.Response;

@Tag(name = "공통 API", description = "공통 기능들에 대한 API")
public interface GlobalControllerDocs {

    @Operation(summary = "공지사항 목록 조회", description = "페이지네이션 값을 통해 공지사항 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                    "status": 200,
                    "message": "공지사항 목록 조회 성공",
                    "data": {
                        "content": [
                            {
                                "id": "공지사항 ID",
                                "title": "제목",
                                "viewCnt": "조회수",
                                "member": {
                                    "id": "회원 ID",
                                    "email": "회원 이메일",
                                    "nickName": "회원 닉네임",
                                    "role": "회원 권한"
                                }
                            }
                        ],
                        "pageable": {
                            "페이징 정보들...": "정보"
                        }
                    }
                }
                """))),
            @ApiResponse(responseCode = "401", description = "공지사항 목록 조회 실패 - 존재하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "공지사항이 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> noticeList(Pageable pageable);

    @Operation(summary = "공지사항 상세 조회", description = "공지사항 ID를 통한 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 상세 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                    "status": 200,
                    "message": "공지사항 목록 조회 성공",
                    "data": {
                        "id": "공지사항 ID",
                        "title": "제목",
                        "content": "내용",
                        "viewCnt": "조회수",
                        "member": {
                            "id": "회원 ID",
                            "email": "회원 이메일",
                            "nickName": "회원 닉네임",
                            "role": "회원 권한"
                        }
                    }
                }
                """))),
            @ApiResponse(responseCode = "401", description = "공지사항 목록 조회 실패 - 존재하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "공지사항이 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> noticeDetail(Long noticeId);

    @Operation(summary = "공지사항 조회수 증가", description = "공지사항 ID를 통해 조회수 증가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회수 증가 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "조회수 증가 성공",
                        "data": "조회수"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "조회수 증가 실패 - 유효하지 않은 공지사항 ID", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "공지사항이 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> addViewCnt(Long noticeId);

    @Operation(summary = "공지사항 댓글 목록 조회", description = "공지사항 ID를 통해 댓글 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 댓글 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "공지사항 댓글 목록 조회 성공",
                        "data": {
                            "id": "댓글 ID",
                            "member": {
                                "id": "회원 ID",
                                "email": "회원 이메일",
                                "nickName": "회원 닉네임",
                                "role": "회원 권한"
                            },
                            "comment": "댓글 내용"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "댓글 목록 조회 실패 - 유효하지 않은 공지사항 ID 또는 댓글이 존재하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "공지사항이 존재하지 않습니다. / 댓글이 존재하지 않습니다.",
                        "data": null
                    }
                    """))),
    })
    ResponseEntity<Response<?>> noticeCommentList(Long noticeId);
}
