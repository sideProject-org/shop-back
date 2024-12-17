package toy.shop.controller.inquiry;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.inquiry.ItemInquiryCommentSaveRequestDTO;
import toy.shop.dto.inquiry.ItemInquiryCommentUpdateRequestDTO;

@Tag(name = "상품 문의 답변 API", description = "상품 문의 답변 기능들에 대한 API")
public interface ItemInquiryCommentControllerDocs {

    @Operation(summary = "상품 문의 답변", description = "request 정보, 사용자 정보를 통해 상품 문의 답변")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 문의 답변 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "상품 문의 답변 성공",
                        "data": "상품 문의 답변 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 문의 답변 실패 - 상품 문의 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품 문의입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 문의 답변 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "해당 상품의 판매자가 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 문의 답변 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "상품 문의 답변 실패 - 상품 문의 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 409,
                        "message": "이미 답변 완료된 문의입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> answerItemInquiry(ItemInquiryCommentSaveRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 문의 답변 수정", description = "상품 문의 답변 ID, request 정보, 사용자 정보를 통해 상품 문의 답변 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 문의 답변 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 문의 답변 수정 성공",
                        "data": "상품 문의 답변 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 문의 답변 수정 실패 - 문의 답변 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 문의 답변입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 문의 답변 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인된 사용자의 답변이 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 문의 답변 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateItemInquiryComment(Long commentId, ItemInquiryCommentUpdateRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 문의 답변 삭제", description = "상품 문의 답변 ID, 사용자 정보를 통해 상품 문의 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 문의 답변 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 문의 답변 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 문의 답변 삭제 실패 - 문의 답변 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품 문의 답변입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 문의 답변 삭제 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인된 사용자의 답변이 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 문의 답변 삭제 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteItemInquiryComment(Long commentId, Authentication authentication);
}
