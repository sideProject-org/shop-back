package toy.shop.service.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import toy.shop.cmmn.exception.ConflictException;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "알 수 없는 오류가 발생했습니다.";

        if (exception.getCause() instanceof ConflictException) {
            errorMessage = exception.getCause().getMessage();
        }

        // 에러 메시지를 클라이언트에 전달하기 위해 리디렉트
        response.sendRedirect("http://localhost:3000/error?message=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }
}
