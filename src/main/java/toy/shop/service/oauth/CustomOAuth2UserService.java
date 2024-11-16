package toy.shop.service.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        } else {
            throw new UnsupportedSocialLoginException("허용되지 않은 소셜 로그인입니다: " + registrationId);
        }

        String socialName = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        Optional<Member> memberOptional = memberRepository.findByEmail(oAuth2Response.getEmail());

        if (memberOptional.isEmpty()) {
            Member member = new Member(
                    oAuth2Response.getEmail(),
                    oAuth2User.getName(),
                    Role.ROLE_USER,
                    oAuth2Response.getProfileUrl(),
                    socialName
            );
            memberRepository.save(member);
        } else {
            Member member = memberOptional.get();
            member.setImagePath(oAuth2Response.getProfileUrl());
            member.setNickName(oAuth2Response.getName());
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
