package toy.shop.controller.admin.notice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.image.NoticeTmpImageDeleteRequestDTO;
import toy.shop.dto.admin.notice.image.NoticeTmpImageResponseDTO;
import toy.shop.dto.admin.notice.SaveNoticeRequestDTO;
import toy.shop.dto.admin.notice.UpdateNoticeRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.admin.notice.NoticeImageService;
import toy.shop.service.admin.notice.NoticeService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notices")
public class NoticeController implements NoticeControllerDocs {

    private final NoticeService noticeService;
    private final NoticeImageService noticeImageService;

    @PostMapping
    public ResponseEntity<Response<?>> saveNotice(@RequestBody @Valid SaveNoticeRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = noticeService.saveNotice(parameter, userDetails);

        return buildResponse(HttpStatus.OK, "공지사항 등록 성공", result);
    }

    @PutMapping
    public ResponseEntity<Response<?>> updateNotice(@RequestBody @Valid UpdateNoticeRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        noticeService.updateNotice(parameter, userDetails);

        return buildResponse(HttpStatus.OK, "공지사항 수정 성공", null);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Response<?>> deleteNotice(@PathVariable("noticeId") Long noticeId, Authentication authentication) {
        UserDetailsImpl memberDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = authentication.getName();

        noticeService.deleteNotice(noticeId, memberDetails);

        return buildResponse(HttpStatus.OK, "공지사항 삭제 성공", null);
    }

    @PostMapping("/images/tmp")
    public ResponseEntity<Response<?>> saveTmpImage(@RequestParam("file") MultipartFile file) {
        if(file == null || file.isEmpty()) {
            return buildResponse(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다.", null);
        }

        NoticeTmpImageResponseDTO result = noticeImageService.saveTemporaryNoticeImage(file);

        return buildResponse(HttpStatus.OK, "임시 이미지 업로드 성공", result);
    }

    @DeleteMapping("/images")
    public ResponseEntity<Response<?>> deleteImage(@RequestBody @Valid NoticeTmpImageDeleteRequestDTO parameter) {
        noticeImageService.deleteNoticeImage(parameter.getImagePath());

        return buildResponse(HttpStatus.OK, "이미지 삭제 성공", null);
    }
}
