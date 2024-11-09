package toy.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import toy.shop.dto.Response;
import toy.shop.dto.jwt.JwtReissueDTO;
import toy.shop.dto.member.LoginRequestDTO;
import toy.shop.dto.member.SignupRequestDTO;

@Tag(name = "공통 API", description = "공통 기능들에 대한 API")
public interface CmmnControllerDocs {

    @Operation(summary = "회원가입", description = "Request 정보를 통해 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "회원가입 성공",
                        "data": "{회원ID}"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "회원가입 실패 - 유효성 검증 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 400,
                        "message": "유효성 검증 오류",
                        "data": {
                            "fieldName": "필드이름",
                            "message": "필드 에러 메시지"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "회원가입 실패 - 존재하는 이메일", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 409,
                        "message": "이미 존재하는 이메일입니다: {이메일}",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> joinMember(SignupRequestDTO parameter);


    @Operation(summary = "로그인", description = "Request 정보를 통해 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "로그인 성공",
                        "data": {
                            "accessToken": "토큰ID",
                            "refreshToken": "토큰ID"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 실패 - 사용자 정보 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "사용자 정보가 잘못 되었습니다.",
                        "data": null
                    }
                    """))),
    })
    ResponseEntity<Response<?>> signIn(LoginRequestDTO parameter);

    @Operation(summary = "토큰 재발급", description = "Request 정보를 통해 토큰 재발급 혹은 재로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "토큰 재발급 성공",
                        "data": {
                            "accessToken": "토큰ID",
                            "refreshToken": "토큰ID"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "토큰 재발급 실패 - 만료되지 않은 토큰", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 400,
                        "message": "토큰이 아직 만료되지 않았습니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "토큰 재발급 실패 - 재로그인", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "재로그인 하세요",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<?> reissue(String requestAccessToken, JwtReissueDTO parameter);

    @Operation(summary = "공지사항 조회수 증가", description = "공지사항 ID를 통해 조회수 증가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회수 증가 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "조회수 증가 성공",
                        "data": "조회수"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "조회수 증가 실패 - 유효하지 않은 공지사항 ID", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "공지사항이 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> addViewCnt(Long noticeId);
}
