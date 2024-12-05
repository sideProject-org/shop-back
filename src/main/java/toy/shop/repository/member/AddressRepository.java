package toy.shop.repository.member;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import toy.shop.domain.member.Address;
import toy.shop.domain.member.Member;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Modifying
    @Query("update Address a set a.defaultType = 'N' where a.member.id = :memberId and a.defaultType = 'Y'")
    void resetDefaultTypeForMember(@Param("memberId") Long memberId);

    List<Address> findAllByMember(Member member);
}
