package toy.shop.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.domain.member.Member;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${path.profileImage}")
    private String profileImagePath;

    /**
     * 회원가입
     * @param parameter SignupRequestDTO
     * @return 회원 ID
     */
    public Long signup(SignupRequestDTO parameter) {
        // 이메일 중복 체크
        memberRepository.findByEmail(parameter.getEmail())
                .ifPresent(existingMember -> {
                    throw new ConflictException("이미 존재하는 이메일입니다: " + parameter.getEmail());
                });

        Member member = Member.builder()
                .email(parameter.getEmail())
                .password(passwordEncoder.encode(parameter.getPassword()))
                .nickName(parameter.getNickname())
                .gender(parameter.getGender())
                .role(parameter.getRole())
                .imagePath(profileImagePath + "/anonymous.png")
                .phoneNumber(parameter.getPhone())
                .build();

        return memberRepository.save(member).getId();
    }
}
