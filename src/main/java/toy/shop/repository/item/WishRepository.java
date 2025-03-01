package toy.shop.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.item.Item;
import toy.shop.domain.item.Wish;
import toy.shop.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    Optional<Wish> findByIdAndMember(Long id, Member member);

    List<Wish> findByMember_Id(Long memberId);

    Boolean existsByItemAndMember(Item item, Member member);
}
