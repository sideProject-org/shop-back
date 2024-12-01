package toy.shop.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.domain.item.ItemImage;
import toy.shop.domain.member.Member;
import toy.shop.dto.item.ItemRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.item.ItemImageRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.MemberRepository;
import toy.shop.service.FileService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    private final FileService fileService;
    private final ItemImageRepository itemImageRepository;

    @Value("${path.itemImage}")
    private String location;

    private final String resourceHandlerItemURL = "/images/itemImage/";

    /**
     * 상품 정보와 이미지를 저장하는 메서드입니다.
     *
     * 이 메서드는 사용자가 업로드한 상품의 세부 정보와 이미지를 데이터베이스에 저장합니다.
     * 상품 상세 이미지는 개별적으로 저장되며, 추가 이미지(썸네일 등)도 함께 저장됩니다.
     * 저장된 상품은 인증된 사용자와 연관됩니다.
     *
     * @param parameter       상품 정보를 담고 있는 DTO 객체.
     *                        (상품명, 상세 내용, 가격, 할인율, 수량, 상세 이미지, 추가 이미지 포함)
     * @param userDetails     현재 인증된 사용자 정보를 담고 있는 객체.
     * @return                저장된 상품의 고유 ID.
     * @throws RuntimeException 파일 업로드 실패 시 예외를 발생시킵니다.
     * @throws UsernameNotFoundException 사용자 정보를 찾을 수 없을 경우 예외를 발생시킵니다.
     */
    @Transactional
    public Long saveItem(ItemRequestDTO parameter, UserDetailsImpl userDetails) {
        // 상품 저장
        String originalFilename = parameter.getItemDetailImage().getOriginalFilename();
        String imgName;

        try {
            imgName = fileService.uploadFile(location, originalFilename, parameter.getItemDetailImage().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패하였습니다.");
        }

        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        Item item = Item.builder()
                .name(parameter.getName())
                .content(parameter.getContent())
                .price(parameter.getPrice())
                .sale(parameter.getSale())
                .quantity(parameter.getQuantity())
                .imagePath(resourceHandlerItemURL + imgName)
                .member(member)
                .build();

        Item savedItem = itemRepository.save(item);

        // 상품 썸네일 이미지 저장
        for (MultipartFile file : parameter.getItemImages()) {
            try {
                imgName = fileService.uploadFile(location, originalFilename, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드에 실패하였습니다.");
            }

            ItemImage itemImage = ItemImage.builder()
                    .item(savedItem)
                    .imagePath(resourceHandlerItemURL + imgName)
                    .build();

            itemImageRepository.save(itemImage);
        }

        return savedItem.getId();
    }

    /**
     * 주어진 아이템 ID에 해당하는 상품 정보를 업데이트합니다.
     * - 상품 등록자와 요청 사용자가 일치하지 않으면 예외를 던집니다.
     * - 기존 이미지를 삭제하고 새로운 이미지를 업로드합니다.
     * - 상품 정보를 수정한 뒤, 데이터베이스에 저장합니다.
     *
     * @param itemId      업데이트할 상품의 고유 ID
     * @param parameter   업데이트 요청 데이터(상품명, 내용, 가격, 할인율, 수량, 이미지 파일 등)를 포함한 DTO
     * @param userDetails 현재 로그인한 사용자의 정보
     * @return 업데이트된 상품의 고유 ID
     * @throws UsernameNotFoundException 존재하지 않는 사용자일 경우 발생
     * @throws NotFoundException         존재하지 않는 상품일 경우 발생
     * @throws AccessDeniedException     상품 등록자가 아닌 사용자가 요청한 경우 발생
     * @throws RuntimeException          파일 업로드 또는 삭제 실패 시 발생
     */
    @Transactional
    public Long updateItem(Long itemId, ItemRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

        if (!item.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("상품 등록자가 아닙니다.");
        }

        String originalFilename = parameter.getItemDetailImage().getOriginalFilename();
        String newImageName;

        try {
            String currentImageName = fileService.extractFileNameFromUrl(item.getImagePath());
            fileService.deleteFile(location, currentImageName);

            newImageName = fileService.uploadFile(location, originalFilename, parameter.getItemDetailImage().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패하였습니다.");
        }

        item.updateItem(
                parameter.getName(),
                parameter.getContent(),
                parameter.getPrice(),
                parameter.getSale(),
                parameter.getQuantity(),
                resourceHandlerItemURL + newImageName
        );

        return item.getId();
    }

    /**
     * 특정 상품과 해당 상품에 연결된 이미지를 삭제하는 메서드입니다.
     *
     * 이 메서드는 주어진 상품 ID를 기반으로 상품을 검색한 후, 해당 상품과 연관된 이미지를 삭제합니다.
     * 상품을 삭제하기 전에 요청한 사용자가 상품 등록자인지 확인하여 권한 검사를 수행합니다.
     *
     * @param itemId          삭제하려는 상품의 고유 ID.
     * @param userDetails     현재 인증된 사용자 정보를 포함하는 객체.
     * @throws NotFoundException       존재하지 않는 상품 ID인 경우 예외를 발생시킵니다.
     * @throws AccessDeniedException   요청 사용자가 상품 등록자가 아닌 경우 예외를 발생시킵니다.
     */
    @Transactional
    public void deleteItem(Long itemId, UserDetailsImpl userDetails) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

        List<ItemImage> itemImages = itemImageRepository.findByItemId(itemId);

        if (!item.getMember().getId().equals(userDetails.getUserId())) {
            throw new AccessDeniedException("상품 등록자가 아닙니다.");
        }

        String detailImageName = fileService.extractFileNameFromUrl(item.getImagePath());
        fileService.deleteFile(location, detailImageName);

        for (ItemImage itemImage : itemImages) {
            String imageName = fileService.extractFileNameFromUrl(itemImage.getImagePath());
            fileService.deleteFile(location, imageName);
        }

        itemRepository.delete(item);
    }
}
