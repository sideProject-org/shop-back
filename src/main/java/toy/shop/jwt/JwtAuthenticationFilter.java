package toy.shop.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import toy.shop.cmmn.exception.JwtAuthenticationException;
import toy.shop.dto.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtExceptionHandler jwtExceptionHandler;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final List<String> EXCLUDED_URLS = Arrays.asList(
            "/swagger-ui/**",
            "/v3/**",
            "/api/global/**",
            "/api/auth/**",
            "/images/**"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 제외할 경로일 경우 필터 건너뛰기
        String requestURI = request.getRequestURI();
        if (EXCLUDED_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token 추출
        String accessToken = jwtProvider.resolveToken(request);

        try {
            if (accessToken == null) {
                throw new JwtAuthenticationException("토큰이 존재하지 않습니다.");
            }

            // 정상 토큰인지 검사
            jwtProvider.validateAccessToken(accessToken);
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            // 예외를 핸들러로 전달하기 위해 필터 체인 진행 차단
            jwtExceptionHandler.commence(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 에러 응답을 작성하여 클라이언트로 반환합니다.
     *
     * @param response  HttpServletResponse 객체
     * @param status    반환할 HTTP 상태 코드
     * @param message   에러 메시지
     * @throws IOException
     */
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response<?> result = Response.builder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
