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

@Tag(name = "장바구니 API", description = "장바구니 기능들에 대한 API")
public interface CartControllerDocs {


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
                    """)))
    })
    ResponseEntity<Response<?>> addCart(CartSaveRequestDTO parameter, Authentication authentication);
}
