package toy.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toy.shop.domain.order.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.member.id = :memberId AND o.status = '4'")
    List<Order> findOrdersByMemberId(@Param("memberId") Long memberId);

}
