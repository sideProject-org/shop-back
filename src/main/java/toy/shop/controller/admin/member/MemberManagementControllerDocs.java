package toy.shop.controller.admin.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import toy.shop.dto.Response;

@Tag(name = "관리자 - 회원관리 API", description = "회원관리 기능들에 대한 API")
public interface MemberManagementControllerDocs {

    @Operation(summary = "회원관리 - 탈퇴", description = "PathVariable 정보를 통해 회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "회원 탈퇴 성공",
                        "data": "회원 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "회원 탈퇴 실패 - 회원 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> withdrawalMember(Long memberId);

    @Operation(summary = "회원관리 - 복원", description = "PathVariable 정보를 통해 회원 복원")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 복원 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "회원 복원 성공",
                        "data": "회원 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "회원 복원 실패 - 회원 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> restoreMember(Long memberId);
}
