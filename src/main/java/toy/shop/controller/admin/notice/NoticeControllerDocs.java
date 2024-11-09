package toy.shop.controller.admin.notice;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.NoticeTmpImageDeleteRequestDTO;
import toy.shop.dto.admin.notice.SaveNoticeRequestDTO;
import toy.shop.dto.admin.notice.UpdateNoticeRequestDTO;

@Tag(name = "관리자 - 공지사항 API", description = "공지사항 기능들에 대한 API")
public interface NoticeControllerDocs {

    @Operation(summary = "공지사항 등록", description = "request 정보를 통해 공지사항 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "공지사항 등록 성공",
                        "data": "공지사항 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "공지사항 등록 실패 - 이미지 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 400,
                        "message": "유효하지 않은 이미지 경로입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "공지사항 등록 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자 아이디입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> saveNotice(SaveNoticeRequestDTO parameter, Authentication authentication);

    @Operation(summary = "공지사항 수정", description = "request 정보를 통해 공지사항 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "공지사항 수정 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "공지사항 수정 실패 - 공지사항 ID 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 공지사항 아이디입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "공지사항 수정 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 403,
                        "message": "공지사항을 작성자가 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "공지사항 등록 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자 아이디입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> updateNotice(UpdateNoticeRequestDTO parameter, Authentication authentication);

    @Operation(summary = "공지사항 삭제", description = "request 정보를 통해 공지사항 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "공지사항 삭제 성공",
                        "data": "공지사항 ID"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "공지사항 등록 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "해당 공지사항 작성자가 아닙니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "공지사항 등록 실패 - 공지사항 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "공지사항이 존재하지 않습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteNotice(Long noticeId, Authentication authentication);

    @Operation(summary = "임시 이미지 등록", description = "file 정보를 통해 임시 이미지 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임시 파일 업로드 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "임시 파일 업로드 성공",
                        "data": {
                            "originalName": "원래 파일 이름",
                            "savedPath": "저장 경로"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "임시 파일 업로드 실패 - 파일 없음", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 400,
                        "message": "파일이 존재하지 않습니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "임시 파일 업로드 실패 - 서버 에러", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 500,
                        "message": "파일 업로드에 실패하였습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> saveTmpImage(MultipartFile file);

    @Operation(summary = "이미지 삭제", description = "file 정보를 통해 이미지 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 200,
                        "message": "파일 삭제 성공",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "파일 삭제 실패 - 이미지 경로 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 400,
                        "message": "유효하지 않은 이미지 경로입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "파일 삭제 실패 - 이미지 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 500,
                        "message": "이미지를 찾을 수 없거나 삭제할 수 없습니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> deleteImage(NoticeTmpImageDeleteRequestDTO parameter);
}
