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
import toy.shop.dto.item.ItemSaveRequestDTO;
import toy.shop.dto.item.ItemUpdateRequestDTO;

@Tag(name = "상품 API", description = "상품 기능들에 대한 API")
public interface ItemControllerDocs {

    @Operation(summary = "상품 등록", description = "request와 file 정보를 통해 상품 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "상품 등록 성공",
                        "data": "상품 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "상품 등록 실패 - 파일 없음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 400,
                        "message": "파일이 존재하지 않습니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "상품 등록 실패 - 서버 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 500,
                        "message": "파일 업로드에 실패하였습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> saveItem(ItemSaveRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 수정", description = "상품 ID와 request, 사용자 정보를 통해 상품 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 수정 성공",
                        "data": "상품 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 수정 실패 - 상품 없음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 수정 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "상품 등록자가 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "상품 수정 실패 - 서버 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 500,
                        "message": "파일 업로드에 실패하였습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateItem(Long itemId, ItemUpdateRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 삭제", description = "상품 ID와 사용자 정보를 통해 상품 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 삭제 실패 - 상품 없음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 삭제 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "상품 등록자가 아닙니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteItem(Long itemId, Authentication authentication);
}
