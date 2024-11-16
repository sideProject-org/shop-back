package toy.shop.service.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import toy.shop.dto.Response;
import toy.shop.dto.jwt.JwtResponseDTO;
import toy.shop.dto.oauth.CustomOAuthUser;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberTokenHelper memberTokenHelper;
    private final String SERVER = "Server";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuthUser customUserDetails = (CustomOAuthUser) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토근 발급 및 Redis에 RefreshToken 저장
        JwtResponseDTO token = memberTokenHelper.generateAndStoreToken(SERVER, customUserDetails.getEmail(), role);

        response.addCookie(createCookie("Authorization", token.getAccessToken()));
        response.addCookie(createCookie("RefreshToken", token.getRefreshToken()));
        response.sendRedirect("http://localhost:3000/");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(ture);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
