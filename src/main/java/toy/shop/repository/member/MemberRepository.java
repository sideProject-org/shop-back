package toy.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일로 회원 찾기
     * @param email
     * @return
     */
    Optional<Member> findByEmail(String email);
}
