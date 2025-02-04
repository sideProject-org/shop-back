package toy.shop.repository.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import toy.shop.domain.item.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAll(Pageable pageable);

    @Query("select i from Item i where i.id = :id and i.deleteType = 'N'")
    Optional<Item> findActiveItemById(Long id);

    @Query("select i from Item i where i.deleteType = 'N'")
    Page<Item> findActiveItems(Pageable pageable);

    @Query("select count(i) from Item i where i.deleteType = 'N'")
    long countActiveItems();
}
