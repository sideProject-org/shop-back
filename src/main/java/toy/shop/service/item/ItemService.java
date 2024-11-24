package toy.shop.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.domain.item.Item;
import toy.shop.domain.item.ItemImage;
import toy.shop.dto.item.ItemSaveRequestDTO;
import toy.shop.repository.item.ItemImageRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.service.FileService;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    private final FileService fileService;

    @Value("${path.itemImage}")
    private String location;

    private final String resourceHandlerItemURL = "/images/itemImage/";

    @Transactional
    public Long saveItem(ItemSaveRequestDTO parameter) {
        String originalFilename = parameter.getImage().getOriginalFilename();
        String imgName;

        log.info("parameter is {}", parameter.toString());

        try {
            imgName = fileService.uploadFile(location, originalFilename, parameter.getImage().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패하였습니다.");
        }

        Item item = Item.builder()
                .name(parameter.getName())
                .content(parameter.getContent())
                .price(parameter.getPrice())
                .sale(parameter.getSale())
                .quantity(parameter.getQuantity())
                .build();

        Item savedItem = itemRepository.save(item);

        ItemImage itemImage = ItemImage.builder()
                .item(savedItem)
                .imagePath(resourceHandlerItemURL + imgName)
                .build();

        itemImageRepository.save(itemImage);

        return savedItem.getId();
    }
}
