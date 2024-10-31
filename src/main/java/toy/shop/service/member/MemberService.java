package toy.shop.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.domain.member.Member;
import toy.shop.dto.member.LoginRequestDTO;
import toy.shop.dto.member.SignupRequestDTO;
import toy.shop.jwt.JwtDTO;
import toy.shop.jwt.JwtProvider;
import toy.shop.repository.member.MemberRepository;
import toy.shop.service.RedisService;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final String SERVER = "Server";

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

    /**
     * 사용자가 로그인할 때 호출되는 메서드.
     * 사용자 인증 후, JWT 액세스 토큰 및 리프레시 토큰을 생성하여 반환합니다.
     *
     * @param parameter 사용자의 이메일과 비밀번호를 포함하는 로그인 요청 DTO
     * @return 생성된 JWT 정보 (액세스 토큰, 리프레시 토큰 포함)
     */
    @Transactional
    public JwtDTO login(LoginRequestDTO parameter) {
        try {
            Authentication authentication = authenticateUser(parameter.getEmail(), parameter.getPassword());
            String email = authentication.getName();
            String authorities = extractAuthorities(authentication);

            // 토큰 생성 및 저장
            return generateAndStoreToken(SERVER, email, authorities);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("사용자 정보가 잘못 되었습니다.");
        }
    }

    /**
     * 사용자 인증을 처리하고 인증된 Authentication 객체를 반환합니다.
     * SecurityContext에 인증 정보를 설정합니다.
     *
     * @param email 사용자의 이메일
     * @param password 사용자의 비밀번호
     * @return 인증된 사용자 정보가 담긴 Authentication 객체
     */
    private Authentication authenticateUser(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * JWT 토큰을 생성하고 리프레시 토큰을 Redis에 저장합니다.
     * 기존에 Redis에 리프레시 토큰이 있을 경우 삭제 후 새로운 토큰을 저장합니다.
     *
     * @param provider 서비스 제공자 정보 (예: "SERVER")
     * @param email 사용자 이메일
     * @param authorities 사용자의 권한 목록을 콤마로 연결한 문자열
     * @return 생성된 JWT 정보 (액세스 토큰 및 리프레시 토큰 포함)
     */
    private JwtDTO generateAndStoreToken(String provider, String email, String authorities) {
        String redisKey = "RT(" + provider + "):" + email;

        // Redis에 기존 RT가 있으면 삭제
        Optional.ofNullable(redisService.getValues(redisKey)).ifPresent(rt -> redisService.deleteValues(redisKey));

        // 새로운 토큰 생성 및 Redis에 저장
        JwtDTO tokenDto = jwtProvider.createToken(email, authorities);
        redisService.setValuesWithTimeout(redisKey, tokenDto.getRefreshToken(), jwtProvider.getTokenExpirationTime(tokenDto.getRefreshToken()));

        return tokenDto;
    }

    /**
     * Authentication 객체에서 사용자의 권한 목록을 추출하여 콤마로 구분된 문자열로 반환합니다.
     *
     * @param authentication 사용자 인증 정보가 담긴 Authentication 객체
     * @return 사용자의 권한 목록을 콤마로 연결한 문자열
     */
    private String extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
