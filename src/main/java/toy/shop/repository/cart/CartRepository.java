package toy.shop.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.etc.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
