package toy.shop.service.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.dto.admin.notice.NoticeTmpImageResponseDTO;
import toy.shop.repository.admin.notice.NoticeImageRepository;
import toy.shop.service.FileService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NoticeImageService {

    private final FileService fileService;

    private final NoticeImageRepository noticeImageRepository;

    @Value("${path.noticeTmpImage}")
    private String tmpLocation;

    @Value("${path.noticeImage}")
    private String location;

    private final String resourceHandlerNoticeTmpURL = "/images/noticeTmpImage/";
    private final String resourceHandlerNoticeURL = "/images/noticeImage/";

    public NoticeTmpImageResponseDTO saveTemporaryNoticeImage(MultipartFile file) {
        String oriImgName = file.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (StringUtils.isEmpty(oriImgName)) {
            throw new IllegalArgumentException();
        }

        try {
            imgName = fileService.uploadFile(tmpLocation, oriImgName, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패하였습니다.");
        }

        imgUrl = resourceHandlerNoticeTmpURL + imgName;

        return NoticeTmpImageResponseDTO.builder()
                .originalName(oriImgName)
                .savedPath(imgUrl)
                .build();
    }

    public void deleteNoticeImage(String filePath) {
        String fileName = extractFileNameFromUrl(filePath);

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 이미지 경로입니다.");
        }

        // 파일 경로가 임시 경로인지 메인 경로인지 확인
        if (filePath.startsWith(resourceHandlerNoticeTmpURL)) {
            // 임시 파일 경로
            fileService.deleteFile(tmpLocation, fileName);
        } else if (filePath.startsWith(resourceHandlerNoticeURL)) {
            // 메인 파일 경로
            fileService.deleteFile(location, fileName);
        } else {
            throw new IllegalArgumentException("알 수 없는 이미지 경로입니다.");
        }
    }

    private String extractFileNameFromUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("/")) {
            return null;
        }
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
}
