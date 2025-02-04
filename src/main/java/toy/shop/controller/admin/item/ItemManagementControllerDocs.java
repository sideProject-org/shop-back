package toy.shop.controller.admin.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import toy.shop.dto.Response;

@Tag(name = "관리자 - 상품관리 API", description = "상품관리 기능들에 대한 API")
public interface ItemManagementControllerDocs {

    @Operation(summary = "상품관리 - 삭제", description = "PathVariable 정보를 통해 상품 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 삭제 실패 - 상품 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """)))
    })
    public ResponseEntity<Response<?>> deleteItem(Long itemId);
}
