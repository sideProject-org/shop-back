package toy.shop.controller.admin.notice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.dto.Response;
import toy.shop.dto.admin.notice.NoticeTmpImageDeleteRequestDTO;
import toy.shop.dto.admin.notice.NoticeTmpImageResponseDTO;
import toy.shop.dto.admin.notice.SaveNoticeRequestDTO;
import toy.shop.service.admin.notice.NoticeImageService;
import toy.shop.service.admin.notice.NoticeService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
public class NoticeController implements NoticeControllerDocs {

    private final NoticeService noticeService;
    private final NoticeImageService noticeImageService;

    @PostMapping("/save-notice")
    public ResponseEntity<Response<?>> saveNotice(@RequestBody @Valid SaveNoticeRequestDTO parameter) {
        Long result = noticeService.saveNotice(parameter);

        return buildResponse(HttpStatus.OK, "공지사항 등록 성공", result);
    }

    @PostMapping("/images/tmp-upload")
    public ResponseEntity<Response<?>> saveTmpImage(@RequestParam("file") MultipartFile file) {
        if(file == null || file.isEmpty()) {
            buildResponse(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다.", null);
        }

        NoticeTmpImageResponseDTO result = noticeImageService.saveTemporaryNoticeImage(file);

        return buildResponse(HttpStatus.OK, "임시 이미지 업로드 성공", result);
    }

    @PostMapping("/images/delete")
    public ResponseEntity<Response<?>> deleteImage(@RequestBody @Valid NoticeTmpImageDeleteRequestDTO parameter) {
        noticeImageService.deleteNoticeImage(parameter.getImagePath());

        return buildResponse(HttpStatus.OK, "이미지 삭제 성공", null);
    }
}
