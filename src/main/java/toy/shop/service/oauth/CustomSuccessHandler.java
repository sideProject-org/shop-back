package toy.shop.service.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.dto.jwt.JwtResponseDTO;
import toy.shop.dto.oauth.CustomOAuthUser;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberTokenHelper memberTokenHelper;
    private final String SERVER = "Server";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 클라이언트 IP 가져오기
        String clientIp = "http://localhost:3000";

        try {
            CustomOAuthUser customUserDetails = (CustomOAuthUser) authentication.getPrincipal();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

            // 토근 발급 및 Redis에 RefreshToken 저장
            JwtResponseDTO token = memberTokenHelper.generateAndStoreToken(SERVER, customUserDetails.getEmail(), role);

            response.sendRedirect(clientIp + "?accessToken=" + token.getAccessToken() + "&refreshToken=" + token.getRefreshToken());
        } catch (ConflictException ex) {
            // 예외 발생 시 클라이언트에 에러 메시지를 전달하기 위해 리디렉트
            response.sendRedirect(clientIp + "/error?message=" + URLEncoder.encode(ex.getMessage(), "UTF-8"));
        }
    }
}
