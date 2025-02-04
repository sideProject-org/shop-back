package toy.shop.controller.cart;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.cart.CartSaveRequestDTO;
import toy.shop.dto.cart.CartUpdateRequestDTO;

@Tag(name = "장바구니 API", description = "장바구니 기능들에 대한 API")
public interface CartControllerDocs {

    @Operation(summary = "장바구니 조회", description = "사용자 정보를 통해 장바구니 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "장바구니 목록 조회 성공",
                        "data": [
                            {
                                "cartId": "장바구니 ID",
                                "quantity": "수량",
                                "item": {
                                    "id": "상품 ID",
                                    "name": "상품명",
                                    "price": "상품 가격",
                                    "sale": "상품 할인율",
                                    "itemImage": "상품 썸네일 경로"
                                }
                            }
                        ]
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "장바구니 목록 조회 실패 - 상품 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "장바구니에 상품이 존재하지 않습니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "장바구니 목록 조회 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """))),
    })
    ResponseEntity<Response<?>> cartList(Authentication authentication);

    @Operation(summary = "장바구니 담기", description = "request와 사용자 정보를 통해 장바구니 담기")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장바구니 담기 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "장바구니 담기 성공",
                        "data": "장바구니 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "장바구니 담기 실패 - 상품 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "장바구니 담기 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "장바구니 담기 실패 - 상품 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 409,
                        "message": "이미 장바구니에 있는 상품입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> addCart(CartSaveRequestDTO parameter, Authentication authentication);

    @Operation(summary = "장바구니 수량 변경", description = "장바구니 ID와 request 정보, 사용자 정보를 통해 장바구니 수량 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장바구니 수량 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "장바구니 수량 변경 성공",
                        "data": "장바구니 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "장바구니 수량 변경 실패 - 상품 및 장바구니 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다. or 존재하지 않는 장바구니 품목입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "장바구니 수량 변경 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인된 사용자의 장바구니 품목이 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "장바구니 수량 변경 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateCart(Long cartId, CartUpdateRequestDTO parameter, Authentication authentication);

    @Operation(summary = "장바구니 삭제", description = "장바구니 ID와 사용자 정보를 통해 장바구니 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장바구니 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "장바구니 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "장바구니 삭제 실패 - 상품 및 장바구니 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다. or 존재하지 않는 장바구니 품목입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "장바구니 삭제 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인된 사용자의 장바구니 품목이 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "장바구니 삭제 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteCart(Long cartId, Authentication authentication);
}
