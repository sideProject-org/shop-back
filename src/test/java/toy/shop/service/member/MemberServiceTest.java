package toy.shop.service.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.domain.Role;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.repository.member.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("정상 가입 테스트")
    public void signup() {
        SignupRequestDTO dto = SignupRequestDTO.builder()
                .email("test@test.com")
                .password("1234")
                .gender("M")
                .nickname("test")
                .role(Role.ROLE_USER)
                .phone("010-1234-5678")
                .build();

        Long result = memberService.signup(dto);

        assertThat(result).isEqualTo(memberRepository.findByEmail("test@test.com").get().getId());
    }

    @Test
    @DisplayName("중복 가입 테스트")
    public void signupConflictEmail() {
        SignupRequestDTO dto = SignupRequestDTO.builder()
                .email("test@test.com")
                .password("1234")
                .gender("M")
                .nickname("test")
                .role(Role.ROLE_USER)
                .phone("010-1234-5678")
                .build();

        SignupRequestDTO dto2 = SignupRequestDTO.builder()
                .email("test@test.com")
                .password("1234")
                .gender("M")
                .nickname("test")
                .role(Role.ROLE_USER)
                .phone("010-1234-5678")
                .build();

        Long result = memberService.signup(dto);

        assertThrows(ConflictException.class, () -> memberService.signup(dto2));
    }
}