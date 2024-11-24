package toy.shop.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.domain.member.Member;
import toy.shop.dto.item.ItemRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.MemberRepository;
import toy.shop.service.FileService;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    private final FileService fileService;

    @Value("${path.itemImage}")
    private String location;

    private final String resourceHandlerItemURL = "/images/itemImage/";

    /**
     * 저장 요청된 상품 데이터를 DB에 저장하고 해당 상품의 설명 이미지를 업로드 후 관련 정보를 저장합니다.
     *
     * @param parameter 아이템 저장 요청 데이터(이름, 내용, 가격, 할인율, 수량, 이미지 파일 등)를 포함한 DTO
     * @return 저장된 아이템의 고유 ID
     * @throws RuntimeException 이미지 업로드 실패 시 발생
     */
    @Transactional
    public Long saveItem(ItemRequestDTO parameter, UserDetailsImpl userDetails) {
        String originalFilename = parameter.getImage().getOriginalFilename();
        String imgName;

        try {
            imgName = fileService.uploadFile(location, originalFilename, parameter.getImage().getBytes());
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

        return savedItem.getId();
    }

    @Transactional
    public Long updateItem(Long itemId, ItemRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

        if (!item.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("상품 등록자가 아닙니다.");
        }

        String originalFilename = parameter.getImage().getOriginalFilename();
        String newImageName;

        try {
            String currentImageName = fileService.extractFileNameFromUrl(item.getImagePath());
            fileService.deleteFile(location, currentImageName);

            newImageName = fileService.uploadFile(location, originalFilename, parameter.getImage().getBytes());
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

    @Transactional
    public void deleteItem(Long itemId, UserDetailsImpl userDetails) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

        if (!item.getMember().getId().equals(userDetails.getUserId())) {
            throw new AccessDeniedException("상품 등록자가 아닙니다.");
        }

        String imageName = fileService.extractFileNameFromUrl(item.getImagePath());
        fileService.deleteFile(location, imageName);

        itemRepository.delete(item);
    }
}
