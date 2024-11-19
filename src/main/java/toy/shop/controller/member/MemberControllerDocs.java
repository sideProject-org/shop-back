package toy.shop.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.dto.Response;
import toy.shop.dto.member.PasswordResetRequestDTO;
import toy.shop.dto.member.UpdateMemberRequestDTO;

@Tag(name = "회원 API", description = "회원 기능들에 대한 API")
public interface MemberControllerDocs {

    @Operation(summary = "회원 정보 수정", description = "Request 정보를 통해 회원 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "회원 정보 변경 성공",
                        "data": "회원 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "회원 정보 변경 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateMember(UpdateMemberRequestDTO parameter, Authentication authentication);

    @Operation(summary = "프로필 사진 수정", description = "Request 정보를 통해 프로필 사진 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 사진 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "프로필 사진 수정 성공",
                        "data": {
                            "originalName": "원래 파일 이름",
                            "savedPath": "저장 경로"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "프로필 사진 수정 실패 - 파일 없음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 400,
                        "message": "파일이 존재하지 않습니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "프로필 사진 수정 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "프로필 사진 수정 실패 - 서버 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 500,
                        "message": "이미지 업로드에 실패하였습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateProfileImage(MultipartFile file, Authentication authentication);

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
