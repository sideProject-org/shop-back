package toy.shop.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import toy.shop.dto.Response;
import toy.shop.dto.member.PasswordResetRequestDTO;

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
    ResponseEntity<?> logout(String requestAccessToken);

    @Operation(summary = "비밀번호 변경 이메일 전송", description = "비밀번호 변경 URL 이메일로 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 전송 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "로그아웃 성공",
                        "data": {
                            "userEmail": "사용자 이메일",
                            "token": "비밀번호 변경 URL을 위한 token 값"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "이메일 전송 실패", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 이메일입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> sendResetPasswordEmail(Authentication authentication);

    @Operation(summary = "비밀번호 변경", description = "request 정보를 통해 비밀번호 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "비밀번호 변경 성공",
                        "data": true
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "비밀번호 변경 실패", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "유효하지 않은 토큰입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> resetPassword(PasswordResetRequestDTO parameter);
}
