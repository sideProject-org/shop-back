package toy.shop.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.item.ItemImage;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
