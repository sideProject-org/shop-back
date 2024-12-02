package toy.shop.repository.item;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import toy.shop.domain.item.ItemImage;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId);

    @Query("SELECT i FROM ItemImage i JOIN FETCH i.item item WHERE item.id IN :itemIds")
    List<ItemImage> findAllByItemIds(@Param("itemIds") List<Long> itemIds);

}
