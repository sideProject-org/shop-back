package toy.shop.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.etc.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsByMemberIdAndItemId(Long memberId, Long itemId);

    List<Cart> findAllByMemberId(Long memberId);
}
