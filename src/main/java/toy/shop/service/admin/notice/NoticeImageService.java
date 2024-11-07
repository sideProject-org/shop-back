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

    /**
     * 공지사항에 임시 이미지 파일을 저장하는 메서드입니다.
     * 업로드된 파일을 지정된 임시 저장 위치에 저장하고, 저장된 이미지의 경로 정보를 반환합니다.
     *
     * @param file 업로드할 이미지 파일 (MultipartFile 형식)
     * @return {@link NoticeTmpImageResponseDTO} 객체로, 원본 파일 이름과 저장된 이미지 경로를 포함합니다.
     * @throws IllegalArgumentException 파일 이름이 비어 있거나 유효하지 않은 경우 발생합니다.
     * @throws RuntimeException 이미지 업로드 중 예외가 발생한 경우 발생합니다.
     */
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
            throw new RuntimeException("이미지 업로드에 실패하였습니다.");
        }

        imgUrl = resourceHandlerNoticeTmpURL + imgName;

        return NoticeTmpImageResponseDTO.builder()
                .originalName(oriImgName)
                .savedPath(imgUrl)
                .build();
    }

    /**
     * 공지사항 이미지 파일을 삭제하는 메서드입니다.
     * 주어진 파일 경로에 따라 이미지가 임시 저장소에 있는지 또는 메인 저장소에 있는지를 판단하여 해당 파일을 삭제합니다.
     *
     * @param filePath 삭제할 이미지의 경로 URL
     * @throws IllegalArgumentException 유효하지 않거나 알 수 없는 이미지 경로인 경우 발생합니다.
     */
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

    /**
     * 이미지 URL에서 파일 이름을 추출하는 메서드입니다.
     *
     * @param imageUrl 파일 이름을 추출할 이미지 URL
     * @return 추출된 파일 이름 또는 유효하지 않은 URL인 경우 null을 반환합니다.
     */
    private String extractFileNameFromUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("/")) {
            return null;
        }
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

}
