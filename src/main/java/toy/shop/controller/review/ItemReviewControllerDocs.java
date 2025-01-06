package toy.shop.controller.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.review.ItemReviewSaveRequestDTO;

@Tag(name = "상품 후기 API", description = "상품 후기 기능들에 대한 API")
public interface ItemReviewControllerDocs {

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
}
