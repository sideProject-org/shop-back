package toy.shop.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.member.address.AddressSaveRequestDTO;

@Tag(name = "배송지 API", description = "회원 배송지에 대한 API")
public interface AddressControllerDocs {

    @Operation(summary = "배송지 등록", description = "request와 authentication 정보를 통해 배송지 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "배송지 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "배송지 등록 성공",
                        "data": "배송지 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "배송지 등록 실패 - 사용자 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> saveAddress(AddressSaveRequestDTO parameter, Authentication authentication);
}
