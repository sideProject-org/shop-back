package toy.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {


}
