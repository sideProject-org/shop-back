package toy.shop.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.item.ItemImage;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId);
}
