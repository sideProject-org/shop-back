package toy.shop.service.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.cmmn.exception.UnsupportedSocialLoginException;
import toy.shop.domain.Role;
import toy.shop.domain.member.Member;
import toy.shop.dto.oauth.*;
import toy.shop.repository.member.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("unsupported_social_login"), "허용되지 않은 소셜 로그인입니다: " + registrationId);
        }

        String socialName = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        Optional<Member> memberOptional = memberRepository.findByEmail(oAuth2Response.getEmail());

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (!member.getSocialName().equals(socialName)) {
                throw new OAuth2AuthenticationException(new OAuth2Error("conflict"), "이미 다른 소셜 계정으로 가입된 이메일입니다: " + oAuth2Response.getEmail());
            }
            member.setImagePath(oAuth2Response.getProfileUrl());
            member.setNickName(oAuth2Response.getName());
        } else {
            Member member = new Member(
                    oAuth2Response.getEmail(),
                    oAuth2Response.getName(),
                    Role.ROLE_USER,
                    oAuth2Response.getProfileUrl(),
                    socialName
            );
            memberRepository.save(member);
        }

        UserDTO userDTO = UserDTO.builder()
                .socialName(socialName)
                .name(oAuth2Response.getName())
                .email(oAuth2Response.getEmail())
                .profileImage(oAuth2Response.getProfileUrl())
                .role(Role.ROLE_USER)
                .build();

        return new CustomOAuthUser(userDTO);
    }
}
