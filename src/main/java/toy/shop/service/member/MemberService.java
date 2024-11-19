package toy.shop.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import toy.shop.cmmn.exception.AccessTokenNotExpiredException;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.domain.member.Member;
import toy.shop.dto.jwt.JwtResponseDTO;
import toy.shop.dto.member.*;
import toy.shop.jwt.JwtProvider;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.member.MemberRepository;
import toy.shop.service.MailService;
import toy.shop.service.RedisService;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final MailService mailService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${path.profileImage}")
    private String profileImagePath;
    private final String SERVER = "Server";

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

    /* ----------------------------------------------------- 로그인 관련 메서드 ----------------------------------------------------- */
    /**
     * 사용자가 로그인할 때 호출되는 메서드.
     * 사용자 인증 후, JWT 액세스 토큰 및 리프레시 토큰을 생성하여 반환합니다.
     *
     * @param parameter 사용자의 이메일과 비밀번호를 포함하는 로그인 요청 DTO
     * @return 생성된 JWT 정보 (액세스 토큰, 리프레시 토큰 포함)
     */
    @Transactional
    public JwtResponseDTO login(LoginRequestDTO parameter) {
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
    public JwtResponseDTO generateAndStoreToken(String provider, String email, String authorities) {
        String redisKey = "RT(" + provider + "):" + email;

        // Redis에 기존 RT가 있으면 삭제
        Optional.ofNullable(redisService.getValues(redisKey)).ifPresent(rt -> redisService.deleteValues(redisKey));

        // 새로운 토큰 생성 및 Redis에 저장
        JwtResponseDTO tokenDto = jwtProvider.createToken(email, authorities);
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
    /* ----------------------------------------------------- 로그인 관련 메서드 ----------------------------------------------------- */

    /* ----------------------------------------------------- 로그아웃 관련 메서드 ----------------------------------------------------- */
    /**
     * 사용자가 로그아웃할 때 호출되는 메서드.
     * 리프레시 토큰을 Redis에서 삭제하고, 액세스 토큰을 로그아웃 상태로 Redis에 저장하여
     * 이후 해당 토큰을 사용하지 못하도록 처리합니다.
     *
     * @param requestAccessTokenInHeader "Bearer {AccessToken}" 형식의 액세스 토큰이 포함된 요청 헤더
     */
    @Transactional
    public void logout(String requestAccessTokenInHeader) {
        String accessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(accessToken);

        // Refresh Token 삭제
        deleteRefreshToken(principal);

        // Access Token 로그아웃 처리
        setAccessTokenAsLoggedOut(accessToken);
    }

    /**
     * Redis에 저장된 리프레시 토큰을 삭제합니다.
     *
     * @param principal 사용자 식별자 (이메일 등)
     */
    private void deleteRefreshToken(String principal) {
        String redisKey = getRefreshTokenKey(principal);
        Optional.ofNullable(redisService.getValues(redisKey))
                .ifPresent(rt -> redisService.deleteValues(redisKey));
    }

    /**
     * Redis에 로그아웃 처리된 Access Token을 저장합니다.
     * 로그아웃 처리된 Access Token은 Redis에 만료 시간과 함께 저장되어 이후 사용이 제한됩니다.
     *
     * @param accessToken 로그아웃할 Access Token
     */
    private void setAccessTokenAsLoggedOut(String accessToken) {
        long expiration = jwtProvider.getTokenExpirationTime(accessToken) - System.currentTimeMillis();
        redisService.setValuesWithTimeout(accessToken, "logout", expiration);
    }

    /**
     * Access Token에서 사용자 식별자(principal)를 추출합니다.
     *
     * @param accessToken 액세스 토큰
     * @return 사용자 식별자
     */
    public String getPrincipal(String accessToken) {
        return jwtProvider.getAuthentication(accessToken).getName();
    }

    /**
     * 요청 헤더에서 "Bearer" 접두사를 제외한 순수 액세스 토큰 값을 추출합니다.
     *
     * @param tokenHeader 요청 헤더의 토큰
     * @return 순수 액세스 토큰 값 또는 null (유효하지 않은 경우)
     */
    public String resolveToken(String tokenHeader) {
        return Optional.ofNullable(tokenHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElse(null);
    }

    /**
     * Redis에 저장된 Refresh Token의 키 형식을 반환합니다.
     * 주어진 사용자 식별자(principal)를 통해 Redis에 저장된 리프레시 토큰 키를 형식화합니다.
     *
     * @param principal 사용자 식별자
     * @return Redis에서 리프레시 토큰에 접근하기 위한 키
     */
    private String getRefreshTokenKey(String principal) {
        return String.format("RT(%s):%s", SERVER, principal);
    }
    /* ----------------------------------------------------- 로그아웃 관련 메서드 ----------------------------------------------------- */

    /* ----------------------------------------------------- 토큰 재발급 관련 메서드 ----------------------------------------------------- */
    /**
     * 사용자의 리프레시 토큰을 활용해 액세스 토큰을 재발급합니다.
     * 기존 리프레시 토큰이 Redis에 저장되어 있는지 확인하고,
     * 유효한 경우 새 액세스 및 리프레시 토큰을 생성하여 반환합니다.
     *
     * @param requestAccessTokenInHeader 요청 헤더에 포함된 액세스 토큰 (Bearer 포함)
     * @param requestRefreshToken 클라이언트가 제공한 리프레시 토큰
     * @return 새로 발급된 JWT 정보 (액세스 토큰 및 리프레시 토큰 포함) 또는 null (유효하지 않은 경우 재로그인 필요)
     */
    @Transactional
    public JwtResponseDTO reissue(String requestAccessTokenInHeader, String requestRefreshToken) {
        String accessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(accessToken);

        // Access Token 만료 여부 확인
        if (!validateAccessTokenExpiry(accessToken)) {
            throw new AccessTokenNotExpiredException("토큰이 아직 만료되지 않았습니다.");
        }

        // Redis에서 리프레시 토큰 유효성 확인
        if (!isRefreshTokenValid(principal, requestRefreshToken)) {
            return null; // 재로그인 요청
        }

        // 인증 정보 설정 및 권한 가져오기
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = extractAuthorities(authentication);

        // 새 토큰 발급 및 Redis 업데이트
        JwtResponseDTO tokenDto = generateAndStoreToken(SERVER, principal, authorities);
        return tokenDto;
    }

    /**
     * Redis에 저장된 리프레시 토큰을 유효성 검사하고,
     * 저장된 리프레시 토큰과 요청된 리프레시 토큰이 일치하는지 확인합니다.
     *
     * @param principal 사용자 식별자
     * @param requestRefreshToken 요청된 리프레시 토큰
     * @return 리프레시 토큰이 유효하고 일치하면 true, 아니면 false
     */
    private boolean isRefreshTokenValid(String principal, String requestRefreshToken) {
        String redisKey = getRefreshTokenKey(principal);
        String refreshTokenInRedis = redisService.getValues(redisKey);

        if (refreshTokenInRedis == null || !jwtProvider.validateRefreshToken(requestRefreshToken) ||
                !refreshTokenInRedis.equals(requestRefreshToken)) {
            redisService.deleteValues(redisKey); // 탈취 가능성으로 인해 삭제
            return false;
        }
        return true;
    }

    /**
     * Access Token의 만료 여부를 확인합니다.
     * 유효한 토큰의 경우 만료 시간을 기준으로 만료 여부를 반환하며,
     * 만료된 경우와 유효하지 않은 경우 모두 만료된 것으로 간주합니다.
     *
     * @param accessToken 만료 여부를 확인할 Access Token
     * @return true - 만료된 토큰 또는 유효하지 않은 토큰, false - 만료되지 않은 토큰
     */
    private boolean validateAccessTokenExpiry(String accessToken) {
        try {
            return jwtProvider.isTokenExpired(accessToken);
        } catch (Exception e) {
            return true; // 토큰 만료 혹은 유효하지 않은 토큰으로 간주
        }
    }
    /* ----------------------------------------------------- 토큰 재발급 관련 메서드 ----------------------------------------------------- */

    /**
     * 비밀번호 재설정 이메일을 발송하는 메서드입니다.
     * 주어진 사용자 세부 정보를 기반으로 사용자의 이메일을 확인한 후, 비밀번호 재설정 이메일을 생성하여 발송합니다.
     * 생성된 고유 토큰과 사용자 이메일을 포함한 응답 DTO를 반환합니다.
     *
     * @param userDetails 사용자 정보를 담고 있는 {@link UserDetailsImpl} 객체.
     *                    이 객체는 사용자의 이메일(사용자 이름) 정보를 포함합니다.
     * @return 비밀번호 재설정 이메일 발송에 대한 정보를 담은 {@link PasswordRestResponseDTO} 객체.
     *         이 DTO는 사용자 이메일과 재설정에 사용되는 UUID 토큰을 포함합니다.
     * @throws UsernameNotFoundException 사용자의 이메일이 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public PasswordRestResponseDTO sendResetEmail(UserDetailsImpl userDetails) {
        if (memberRepository.existsByEmail(userDetails.getUsername())) {
            String uuid = mailService.generateResetEmail(userDetails.getUsername());

            return PasswordRestResponseDTO.builder()
                    .userEmail(userDetails.getUsername())
                    .token(uuid)
                    .build();
        } else {
            throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
        }
    }


    /**
     * 비밀번호 재설정을 수행하는 메서드입니다.
     * 요청된 토큰의 유효성을 검증하고, 유효한 경우 해당 사용자의 비밀번호를 재설정합니다.
     * 이후 사용된 토큰은 무효화됩니다.
     *
     * @param parameter 비밀번호 재설정을 위한 요청 정보가 담긴 {@link PasswordResetRequestDTO} 객체.
     *                  이 객체는 사용자의 이메일, 재설정할 비밀번호, 그리고 재설정 토큰을 포함합니다.
     * @return 비밀번호 재설정 성공 여부. 유효한 토큰을 사용하여 비밀번호가 성공적으로 변경되면 true를 반환합니다.
     * @throws BadCredentialsException 유효하지 않은 토큰이 제공될 경우 발생합니다.
     */
    @Transactional
    public boolean resetPassword(PasswordResetRequestDTO parameter) {
        String token = parameter.getToken();

        if (!redisService.isValidPwResetToken(token)) {
            throw new BadCredentialsException("유효하지 않은 토큰입니다.");
        }

        // 비밀번호 재설정
        Optional<Member> memberOptional = memberRepository.findByEmail(parameter.getUserEmail());
        memberOptional.ifPresent(member -> {
            member.setPassword(passwordEncoder.encode(parameter.getPassword()));
            memberRepository.save(member);
        });

        // 토큰 무효화
        redisService.invalidatePwResetToken(token);

        return true;
    }

    /**
     * 제공된 파라미터를 기반으로 회원 정보를 업데이트합니다.
     *
     * <p>이 메서드는 현재 사용자의 ID에 해당하는 회원 엔티티를 조회합니다.
     * 회원이 존재할 경우, {@code UpdateMemberRequestDTO}를 사용하여 회원 정보를
     * 업데이트하고, 회원의 ID를 반환합니다. 회원을 찾을 수 없는 경우,
     * {@code UsernameNotFoundException}이 발생합니다.</p>
     *
     * <p>메서드는 {@code @Transactional}로 어노테이션되어 있으며, 이로 인해
     * 전체 업데이트 작업이 트랜잭션 컨텍스트 내에서 수행됩니다. 실행 중 예외가
     * 발생하면 트랜잭션은 롤백됩니다.</p>
     *
     * @param parameter 업데이트할 회원 정보가 포함된 데이터 전송 객체
     * @param userDetails 현재 사용자의 세부 정보를 포함하는 객체
     * @return 업데이트된 회원의 ID
     * @throws UsernameNotFoundException 회원을 찾을 수 없는 경우 발생
     */

    @Transactional
    public long updateMember(UpdateMemberRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        member.updateMember(parameter);

        return member.getId();
    }
}
