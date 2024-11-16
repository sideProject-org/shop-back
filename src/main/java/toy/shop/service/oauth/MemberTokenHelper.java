package toy.shop.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toy.shop.dto.jwt.JwtResponseDTO;
import toy.shop.jwt.JwtProvider;
import toy.shop.service.RedisService;

@Component
@RequiredArgsConstructor
public class MemberTokenHelper {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public JwtResponseDTO generateAndStoreToken(String provider, String email, String role) {
        String redisKey = "RT(" + provider + "):" + email;
        redisService.deleteValues(redisKey);

        JwtResponseDTO tokenDto = jwtProvider.createToken(email, role);
        redisService.setValuesWithTimeout(redisKey, tokenDto.getRefreshToken(), jwtProvider.getTokenExpirationTime(tokenDto.getRefreshToken()));
        return tokenDto;
    }
}
