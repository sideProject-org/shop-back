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
                        "page": {
                            "size": "페이징 사이즈",
                            "number": "페이지 넘버",
                            "totalElements": "총 개수",
                            "totalPages": "총 페이지수"
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

    @Operation(summary = "상품 목록 조회", description = "페이지네이션 값을 통해 상품 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                    "status": 200,
                    "message": "상품 목록 조회 성공",
                    "data": {
                        "content": [
                            {
                                "id": "상품 ID",
                                "name": "상품명",
                                "price": "상품 정가",
                                "sale": "할인율",
                                "itemImages": [
                                    "이미지 경로"
                                ]
                            }
                        ],
                        "page": {
                            "size": "페이징 사이즈",
                            "number": "페이지 넘버",
                            "totalElements": "총 개수",
                            "totalPages": "총 페이지수"
                        }
                    }
                }
                """))),
            @ApiResponse(responseCode = "401", description = "상품 목록 조회 실패 - 존재하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "상품이 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> itemList(Pageable pageable);

    @Operation(summary = "상품 상세 조회", description = "상품 ID를 통한 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 상세정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                    "status": 200,
                    "message": "상품 상세정보 조회 성공",
                    "data": {
                        "id": "상품 ID",
                        "name": "상품명",
                        "price": "상품 정가",
                        "sale": "할인율",
                        "content": "상품 상세정보 부가 설명",
                        "imageDetail": "상품 상세정보 이미지 경로",
                        "imageList": [
                            "상품 이미지 경로"
                        ]
                    }
                }
                """))),
            @ApiResponse(responseCode = "401", description = "상품 상세정보 조회 실패 - 존재하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "상품이 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> itemDetail(Long itemId);

    @Operation(summary = "상품 문의 목록 조회", description = "상품 ID, 페이지네이션 값을 통해 상품 문의 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 문의 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                    "status": 200,
                    "message": "상품 문의 목록 조회 성공",
                    "data": {
                        "content": [
                            {
                                "id": "상품 문의 ID",
                                "title": "상품 문의 제목",
                                "content": "상품 문의 내용",
                                "answerStatus": "답변 여부",
                                "nickname": "사용자 닉네임",
                                "createdAt": "등록 일자"
                            }
                        ],
                        "page": {
                            "size": "페이징 사이즈",
                            "number": "페이지 넘버",
                            "totalElements": "총 개수",
                            "totalPages": "총 페이지수"
                        }
                    }
                }
                """))),
            @ApiResponse(responseCode = "401", description = "상품 문의 목록 조회 실패 - 존재하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "상품 문의가 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> itemInquiryList(Long itemId,Pageable pageable);
}
