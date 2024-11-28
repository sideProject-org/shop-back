package toy.shop.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.dto.item.image.ItemTmpImageResponseDTO;
import toy.shop.repository.item.ItemImageRepository;
import toy.shop.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemImageService {

    private final FileService fileService;

    private final ItemImageRepository itemImageRepository;

    @Value("${path.itemTmpImage}")
    private String tmpLocation;

    @Value("${path.itemImage}")
    private String location;

    private final String resourceHandlerItemTmpURL = "/images/itemTmpImage/";
    private final String resourceHandlerItemURL = "/images/itemImage/";

    /**
     * 다중 이미지 파일을 임시 저장소에 업로드하고 각 파일의 저장 경로와 원본 이름 정보를 반환합니다.
     *
     * @param files 업로드할 {@link MultipartFile} 리스트
     * @return 각 파일의 원본 이름과 저장 경로를 담은 {@link ItemTmpImageResponseDTO} 리스트
     * @throws RuntimeException 파일 업로드 실패 시 발생
     */
    public List<ItemTmpImageResponseDTO> saveTemporaryItemImage(List<MultipartFile> files) {
        List<ItemTmpImageResponseDTO> responseDTOList = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalImageName = file.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";

            try {
                imgName = fileService.uploadFile(tmpLocation, originalImageName, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드에 실패하였습니다.");
            }

            imgUrl = resourceHandlerItemTmpURL + imgName;

            // 처리된 파일 정보를 DTO로 생성하고 리스트에 추가
            ItemTmpImageResponseDTO dto = ItemTmpImageResponseDTO.builder()
                    .originalName(originalImageName)
                    .savedPath(imgUrl)
                    .build();

            responseDTOList.add(dto);
        }

        return responseDTOList;
    }
}
