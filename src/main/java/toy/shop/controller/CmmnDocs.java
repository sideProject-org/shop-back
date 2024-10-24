package toy.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import toy.shop.dto.Response;
import toy.shop.dto.member.SignupRequestDTO;

@Tag(name = "공통 기능 API", description = "공통 기능 로직에 관한 API")
public interface CmmnDocs {

    @Operation(summary = "회원가입", description = "Request 정보를 통해 회원가입")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
//                    @ExampleObject(name = "회원가입 성공", value = """
//                    {
//                        "status": 200,
//                        "message": "회원가입 성공",
//                        "data": {회원ID}
//                    }
//                    """)
//            })),
//            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
//                    @ExampleObject(name = "회원가입 실패 - 유효성 검증 오류", value = """
//                    {
//                        "status": 400,
//                        "message": "유효성 검증 오류",
//                        "data": {
//                            "fieldName": "필드이름",
//                            "message": "필드 에러 메시지"
//                        }
//                    }
//                    """)
//            })),
//            @ApiResponse(responseCode = "409", content = @Content(mediaType = "application/json", examples = {
//                    @ExampleObject(name = "회원가입 실패 - 존재하는 이메일", value = """
//                    {
//                        "status": 409,
//                        "message": "이미 존재하는 이메일입니다: {이메일}",
//                        "data": null
//                    }
//                    """)
//            }))
//    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
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
}
