package toy.shop.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.item.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
