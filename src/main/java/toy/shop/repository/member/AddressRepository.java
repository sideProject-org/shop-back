package toy.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.member.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
