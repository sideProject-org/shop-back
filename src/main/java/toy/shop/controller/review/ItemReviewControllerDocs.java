package toy.shop.controller.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.review.ItemReviewSaveRequestDTO;
import toy.shop.dto.review.ItemReviewUpdateRequestDTO;

@Tag(name = "상품 후기 API", description = "상품 후기 기능들에 대한 API")
public interface ItemReviewControllerDocs {

    @Operation(summary = "상품 후기 목록 조회", description = "상품 ID, 페이지네이션 값을 통해 상품 후기 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 후기 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 후기 목록 조회 성공",
                        "data": ""
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 후기 목록 조회 실패 - 상품 후기 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "상품 후기가 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> itemReviewList(Long itemId, Pageable pageable);

    @Operation(summary = "상품 후기 등록", description = "request와 file, 사용자 정보를 통해 상품 후기 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 후기 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "상품 후기 등록 성공",
                        "data": "상품 후기 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 후기 등록 실패 - 상품 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 후기 등록 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "상품을 구매하지 않은 사용자입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 후기 등록 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "상품 후기 등록 실패 - 업로드 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 500,
                        "message": "이미지 업로드에 실패하였습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> registerItemReview(ItemReviewSaveRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 후기 수정", description = "request와 file, 사용자 정보를 통해 상품 후기 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 후기 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 후기 수정 성공",
                        "data": "상품 후기 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 후기 수정 실패 - 상품 후기 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품 후기입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 후기 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인 된 사용자의 상품 후기가 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 후기 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "상품 후기 등록 실패 - 업로드 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 500,
                        "message": "이미지 업로드에 실패하였습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateItemReview(ItemReviewUpdateRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 후기 삭제", description = "상품 후기 ID, 사용자 정보를 통해 상품 후기 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 후기 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 후기 삭제 성공",
                        "data": "null"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 후기 수정 실패 - 상품 후기 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품 후기입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 후기 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인 된 사용자의 상품 후기가 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 후기 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteItemReview(Long itemReviewId, Authentication authentication);
}
