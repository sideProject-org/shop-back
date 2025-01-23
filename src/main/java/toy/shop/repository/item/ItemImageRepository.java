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

    @Query("SELECT ii FROM ItemImage ii WHERE ii.item.id IN :itemIds AND ii.id IN " +
            "(SELECT MIN(ii2.id) FROM ItemImage ii2 WHERE ii2.item.id IN :itemIds GROUP BY ii2.item.id)")
    List<ItemImage> findFirstImageByItemIds(@Param("itemIds") List<Long> itemIds);

    @Query("SELECT ii FROM ItemImage ii WHERE ii.item.id IN :itemId AND ii.id IN " +
            "(SELECT MIN(ii2.id) FROM ItemImage ii2 WHERE ii2.item.id IN :itemId GROUP BY ii2.item.id)")
    ItemImage findFirstImageByItemId(Long itemId);

    void deleteAllByItemId(Long itemId);
}
