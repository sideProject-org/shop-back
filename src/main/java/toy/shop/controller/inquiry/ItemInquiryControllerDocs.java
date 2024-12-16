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
import toy.shop.dto.inquiry.ItemInquiryRequestDTO;

@Tag(name = "상품 문의 API", description = "상품 문의 기능들에 대한 API")
public interface ItemInquiryControllerDocs {

    @Operation(summary = "상품 문의 등록", description = "request 정보, 사용자 정보를 통해 상품 문의 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 문의 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "상품 문의 등록 성공",
                        "data": "상품 문의 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 문의 등록 실패 - 상품 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 문의 등록 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> registerItemInquiry(ItemInquiryRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 문의 수정", description = "상품 문의 ID, request 정보, 사용자 정보를 통해 상품 문의 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 문의 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 문의 수정 성공",
                        "data": "상품 문의 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 문의 수정 실패 - 문의 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 문의글입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 문의 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인된 사용자의 문의글이 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 문의 수정 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateItemInquiry(Long itemInquiryId, ItemInquiryRequestDTO parameter, Authentication authentication);

    @Operation(summary = "상품 문의 삭제", description = "상품 문의 ID, 사용자 정보를 통해 상품 문의 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 문의 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "상품 문의 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "상품 문의 삭제 실패 - 문의 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 문의글입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "상품 문의 삭제 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인된 사용자의 문의글이 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품 문의 삭제 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteItemInquiry(Long itemInquiryId, Authentication authentication);
}
