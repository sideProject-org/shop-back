package toy.shop.service.admin.notice;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.domain.notice.Notice;
import toy.shop.domain.notice.NoticeImage;
import toy.shop.dto.admin.notice.NoticeTmpImageResponseDTO;
import toy.shop.repository.admin.notice.NoticeImageRepository;
import toy.shop.service.FileService;

import java.io.IOException;
import java.util.List;

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
     * 임시 이미지 URL을 메인 이미지 URL로 변환하는 메서드입니다.
     * 주어진 HTML 내용에서 모든 `<img>` 태그를 찾아 `src` 속성이 임시 URL로 시작하는 경우
     * 메인 URL로 변환합니다.
     *
     * @param content 변환할 HTML 내용 (임시 이미지 URL이 포함된 문자열)
     * @return 변환된 HTML 내용 (메인 이미지 URL이 적용된 문자열)
     */
    public String convertTemporaryUrlsToMainUrls(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        // HTML 파싱
        Document document = Jsoup.parse(content);

        // 모든 img 태그를 선택
        Elements imgTags = document.select("img");

        // 각 img 태그의 src 속성 검사 및 변환
        for (Element imgTag : imgTags) {
            String src = imgTag.attr("src");
            if (src.startsWith(resourceHandlerNoticeTmpURL)) {
                String updatedSrc = src.replace(resourceHandlerNoticeTmpURL, resourceHandlerNoticeURL);
                imgTag.attr("src", updatedSrc);
            }
        }

        // 변환된 HTML 반환
        return document.body().html();
    }

    /**
     * 임시 이미지 파일을 메인 저장소로 이동하고, 공지사항과 연관된 이미지 정보를 저장하는 메서드입니다.
     *
     * @param notice 공지사항 객체. 이미지 정보를 연관시키기 위해 사용됩니다.
     * @param tempImageUrls 임시 이미지 URL 목록. 이 목록의 각 이미지를 메인 저장소로 이동합니다.
     * @throws IllegalArgumentException 유효하지 않은 이미지 경로가 제공된 경우 발생합니다.
     */
    public void moveTemporaryImagesToMain(Notice notice, List<String> tempImageUrls) {
        for (String tempImageUrl : tempImageUrls) {
            String fileName = extractFileNameFromUrl(tempImageUrl);

            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 이미지 경로입니다.");
            }

            // 파일을 임시 저장소에서 메인 저장소로 이동
            fileService.moveFile(tmpLocation, location, fileName);

            // 메인 URL 생성
            String mainImageUrl = resourceHandlerNoticeURL + fileName;

            // 이미지 정보를 Notice와 연결하여 DB에 저장
            NoticeImage noticeImage = NoticeImage.builder()
                    .notice(notice)
                    .imagePath(mainImageUrl)
                    .build();

            noticeImageRepository.save(noticeImage);
        }
    }

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
     * 공지사항 ID를 기반으로 데이터베이스에 저장된 모든 NoticeImage 엔티티를 삭제하고,
     * 해당 이미지 파일을 파일 시스템에서 삭제하는 메서드입니다.
     *
     * 이 메서드는 주어진 공지사항 ID에 연관된 모든 공지사항 이미지 레코드를 조회한 후,
     * 각 이미지의 파일 경로를 기반으로 실제 파일을 삭제합니다. 모든 파일 삭제가 완료된 후,
     * 데이터베이스에서 해당 이미지 레코드를 삭제합니다.
     *
     * @param noticeId 공지사항 ID (null이 아니어야 함)
     * @throws IllegalArgumentException 공지사항 ID가 null일 경우 발생합니다.
     */
    public void deleteNoticeImageDB(Long noticeId) {
        if (noticeId == null) {
            throw new IllegalArgumentException("공지사항 ID를 기입해주세요.");
        }
        List<NoticeImage> images = noticeImageRepository.findByNoticeId(noticeId);

        for (NoticeImage image : images) {
            deleteNoticeImage(image.getImagePath());
        }

        noticeImageRepository.deleteByNoticeId(noticeId);
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
