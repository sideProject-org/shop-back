package toy.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.order.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
