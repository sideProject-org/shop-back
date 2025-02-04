package toy.shop.service.admin.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.repository.item.ItemImageRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.service.FileService;

@Service
@RequiredArgsConstructor
public class ItemManagementService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final FileService fileService;

    @Value("${path.itemImage}")
    private String location;

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = getItemById(itemId);
        item.deleteItem();
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));
    }
}
