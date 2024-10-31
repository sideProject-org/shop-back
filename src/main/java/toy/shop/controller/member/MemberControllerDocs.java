package toy.shop.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "회원 기능 API", description = "회원 기능 로직에 관한 API")
public interface MemberControllerDocs {

    @Operation(summary = "로그아웃", description = "Request 정보를 통해 로그아웃")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "로그아웃 성공",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken);
}
