package toy.shop.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.domain.item.ItemImage;
import toy.shop.domain.member.Member;
import toy.shop.dto.item.ItemDetailResponseDTO;
import toy.shop.dto.item.ItemListResponseDTO;
import toy.shop.dto.item.ItemSaveRequestDTO;
import toy.shop.dto.item.ItemUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.item.ItemImageRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.MemberRepository;
import toy.shop.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    private final FileService fileService;

    @Value("${path.itemImage}")
    private String location;

    private final String resourceHandlerItemURL = "/images/itemImage/";

    /**
     * 페이지 단위로 상품 목록을 조회하고, 각 상품의 이미지 경로를 포함한 DTO 리스트를 반환합니다.
     *
     * @param pageable 페이징 정보를 포함한 {@link Pageable} 객체
     * @return {@link Page} 객체로 반환된 {@link ItemListResponseDTO} 리스트
     * @throws NotFoundException 조회된 상품이 없을 경우 발생
     */
    @Transactional(readOnly = true)
    public Page<ItemListResponseDTO> itemList(Pageable pageable) {
        long totalCount = itemRepository.count();
        if (totalCount == 0) {
            throw new NotFoundException("상품이 존재하지 않습니다.");
        }

        Page<Item> itemList = itemRepository.findAll(pageable);

        if (itemList.getContent().isEmpty() && pageable.getPageNumber() > 0) {
            int lastPage = itemList.getTotalPages() - 1;
            if (lastPage < 0) {
                throw new NotFoundException("상품이 존재하지 않습니다.");
            }

            Pageable correctedPageable = PageRequest.of(lastPage, pageable.getPageSize(), pageable.getSort());
            itemList = itemRepository.findAll(correctedPageable);
        }

        Map<Long, String> itemImageMap = itemImageRepository.findFirstImageByItemIds(
                itemList.stream().map(Item::getId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(
                itemImage -> itemImage.getItem().getId(),
                ItemImage::getImagePath
        ));

        Page<ItemListResponseDTO> result = itemList.map(item -> ItemListResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .sale(item.getSale())
                .itemImage(itemImageMap.getOrDefault(item.getId(), null))
                .build());

        return result;
    }

    /**
     * 특정 상품의 상세 정보를 조회하여 반환합니다.
     *
     * @param itemId 조회할 상품의 ID
     * @return {@link ItemDetailResponseDTO} 객체로, 상품의 상세 정보와 이미지 경로 리스트를 포함
     * @throws NotFoundException 요청한 상품이 존재하지 않을 경우 발생
     */

    @Transactional(readOnly = true)
    public ItemDetailResponseDTO itemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("상품이 존재하지 않습니다."));
        List<ItemImage> itemImages = itemImageRepository.findByItemId(item.getId());
        List<String> itemImagesPath = itemImages.stream().map(ItemImage::getImagePath).collect(Collectors.toList());

        return ItemDetailResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .sale(item.getSale())
                .content(item.getContent())
                .itemDescriptionImage(item.getImagePath())
                .imageList(itemImagesPath)
                .build();
    }

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
    public Long saveItem(ItemSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        // 상품 저장
        String originalFilename = parameter.getItemDescriptionImage().getOriginalFilename();
        String imgName = uploadFile(parameter.getItemDescriptionImage());

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
     * 기존 상품의 세부 정보(이미지 및 메타데이터 포함)를 업데이트합니다.
     *
     * @param itemId 업데이트할 상품의 ID
     * @param parameter 업데이트할 상품 정보와 이미지를 포함한 DTO
     * @param userDetails 인증된 사용자 정보
     * @return 업데이트된 상품의 ID
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 발생
     * @throws NotFoundException 상품이 존재하지 않을 경우 발생
     * @throws AccessDeniedException 인증된 사용자가 해당 상품의 소유자가 아닐 경우 발생
     * @throws RuntimeException 파일 업로드 또는 삭제 중 오류가 발생할 경우 발생
     */
    @Transactional
    public Long updateItem(Long itemId, ItemUpdateRequestDTO parameter, UserDetailsImpl userDetails) {
        // 사용자와 상품 검증
        Member member = getMemberById(userDetails.getUserId());
        Item item = getItemById(itemId);
        validateItemOwnership(item, member);

        // 상품 이미지 업데이트
        updateItemImages(itemId, item, parameter);

        // 상세 이미지 업데이트
        parameter.getItemDetailImage().ifPresent(detailImage -> updateDetailImage(item, detailImage));

        // 상품 정보 업데이트
        item.updateItem(
                parameter.getName(),
                parameter.getContent(),
                parameter.getPrice(),
                parameter.getSale(),
                parameter.getQuantity()
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
        Member member = getMemberById(userDetails.getUserId());
        Item item = getItemById(itemId);
        validateItemOwnership(item, member);

        List<ItemImage> itemImages = itemImageRepository.findByItemId(itemId);

        String detailImageName = fileService.extractFileNameFromUrl(item.getImagePath());
        fileService.deleteFile(location, detailImageName);

        for (ItemImage itemImage : itemImages) {
            String imageName = fileService.extractFileNameFromUrl(itemImage.getImagePath());
            fileService.deleteFile(location, imageName);
        }

        itemImageRepository.deleteAllByItemId(itemId);
        itemRepository.delete(item);
    }

    private Member getMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));
    }

    private void validateItemOwnership(Item item, Member member) {
        if (!item.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("상품 등록자가 아닙니다.");
        }
    }

    private void updateItemImages(Long itemId, Item item, ItemUpdateRequestDTO parameter) {
        List<MultipartFile> itemImages = parameter.getItemImages();
        if (itemImages != null && itemImages.stream().anyMatch(file -> file != null && !file.isEmpty())) {
            // 기존 이미지 삭제
            deleteExistingItemImages(itemId);

            // 새로운 이미지 등록
            for (MultipartFile file : itemImages) {
                String newImagePath = uploadFile(file);
                ItemImage itemImage = ItemImage.builder()
                        .item(item)
                        .imagePath(resourceHandlerItemURL + newImagePath)
                        .build();
                itemImageRepository.save(itemImage);
            }
        }
    }

    private void deleteExistingItemImages(Long itemId) {
        List<ItemImage> existingImages = itemImageRepository.findByItemId(itemId);
        for (ItemImage itemImage : existingImages) {
            String currentImageName = fileService.extractFileNameFromUrl(itemImage.getImagePath());
            fileService.deleteFile(location, currentImageName);
        }
        itemImageRepository.deleteAllByItemId(itemId);
    }

    private void updateDetailImage(Item item, MultipartFile detailImage) {
        if (!detailImage.isEmpty()) {
            // 기존 이미지 삭제
            String currentImageName = fileService.extractFileNameFromUrl(item.getImagePath());
            fileService.deleteFile(location, currentImageName);

            // 새로운 이미지 등록
            String newImagePath = uploadFile(detailImage);
            item.updateImagePath(resourceHandlerItemURL + newImagePath);
        }
    }

    private String uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            return fileService.uploadFile(location, originalFilename, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패하였습니다.", e);
        }
    }
}
