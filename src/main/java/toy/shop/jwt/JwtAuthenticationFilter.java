package toy.shop.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.IncorrectClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import toy.shop.dto.Response;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Access Token 추출
        String accessToken = jwtProvider.resolveToken(request);

        try { // 정상 토큰인지 검사
            if (accessToken != null && jwtProvider.validateAccessToken(accessToken)) {
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IncorrectClaimException e) { // 잘못된 토큰일 경우
            ObjectMapper objectMapper = new ObjectMapper();
            SecurityContextHolder.clearContext();
            Response<?> result = Response.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();

            extracted(response, HttpStatus.UNAUTHORIZED, objectMapper.writeValueAsString(result));
        } catch (UsernameNotFoundException e) { // 회원을 찾을 수 없을 경우
            ObjectMapper objectMapper = new ObjectMapper();
            SecurityContextHolder.clearContext();
            Response<?> result = Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();

            extracted(response, HttpStatus.NOT_FOUND, objectMapper.writeValueAsString(result));
        }

        filterChain.doFilter(request, response);
    }

    private static void extracted(HttpServletResponse response, HttpStatus unauthorized, String objectMapper) throws IOException {
        response.setStatus(unauthorized.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper);
    }
}
