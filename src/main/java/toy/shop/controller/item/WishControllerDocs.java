package toy.shop.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.item.WishSaveRequestDTO;

@Tag(name = "찜 API", description = "찜 기능들에 대한 API")
public interface WishControllerDocs {

    @Operation(summary = "찜 목록 조회", description = "사용자 정보를 통해 찜 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "찜 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                    "status": 200,
                    "message": "찜 목록 조회 성공",
                    "data": [
                        {
                            "id": "상품 ID",
                            "name": "상품명",
                            "price": "상품 정가",
                            "sale": "할인율",
                            "itemImages": [
                                "이미지 경로"
                            ]
                        }
                    ]
                }
                """)))
    })
    ResponseEntity<Response<?>> wishList(Authentication authentication);

    @Operation(summary = "찜 등록", description = "request와 사용자 정보를 통해 찜 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "찜 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "찜 등록 성공",
                        "data": "찜 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "찜 등록 실패 - 상품 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "찜 등록 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> registerWish(WishSaveRequestDTO parameter, Authentication authentication);

    @Operation(summary = "찜 삭제", description = "wishId와 사용자 정보를 통해 찜 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "찜 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "찜 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "찜 삭제 실패 - 상품 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "찜 삭제 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인된 사용자의 찜 상품이 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "찜 삭제 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteWish(Long wishId, Authentication authentication);
}
