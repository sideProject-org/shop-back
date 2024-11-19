package toy.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.shop.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일 주소를 기반으로 회원 정보를 조회하는 메서드입니다.
     *
     * @param email 조회할 회원의 이메일 주소
     * @return 주어진 이메일 주소를 가진 회원을 Optional로 반환하며, 존재하지 않으면 Optional.empty()를 반환합니다.
     */
    Optional<Member> findByEmail(String email);

    /**
     * 주어진 이메일 주소를 가진 회원이 존재하는지 확인하는 메서드입니다.
     *
     * @param email 존재 여부를 확인할 이메일 주소
     * @return 해당 이메일 주소를 가진 회원이 존재하면 true, 존재하지 않으면 false를 반환합니다.
     */
    boolean existsByEmail(String email);
}
