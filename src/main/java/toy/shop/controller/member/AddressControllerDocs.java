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
import toy.shop.dto.member.address.AddressUpdateRequestDTO;

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

    @Operation(summary = "배송지 수정", description = "배송지 ID와 request, 사용자 정보를 통해 배송지 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송지 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "배송지 수정 성공",
                        "data": "배송지 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "배송지 수정 실패 - 배송지 없음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 배송지입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "배송지 수정 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인 된 회원의 배송지가 아닙니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateAddress(Long addressId, AddressUpdateRequestDTO parameter, Authentication authentication);

    @Operation(summary = "배송지 삭제", description = "배송지 ID와 사용자 정보를 통해 배송지 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송지 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "배송지 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "배송지 삭제 실패 - 배송지 없음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 배송지입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "배송지 삭제 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "로그인 된 회원의 배송지가 아닙니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteAddress(Long addressId, Authentication authentication);
}
