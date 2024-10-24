package toy.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
