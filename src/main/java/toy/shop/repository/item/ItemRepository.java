package toy.shop.repository.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import toy.shop.domain.item.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAll(Pageable pageable);

    @Query("select i from Item i where i.deleteType <> 'Y'")
    Page<Item> findAllActiveItems(Pageable pageable);
}
