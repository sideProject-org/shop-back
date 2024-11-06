package toy.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final String TOKEN_PREFIX = "password-reset-token: ";
    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    // 만료시간 설정 -> 자동 삭제
    public void setValuesWithTimeout(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public String getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    /* ====================================== 비밀번호 변경 토큰 로직 ====================================== */
    /**
     * 비밀번호 재설정 토큰을 생성하는 메서드입니다.
     * UUID 기반으로 고유한 토큰을 생성하며, 생성된 토큰을 Redis에 저장하여 유효성을 유지합니다.
     *
     * @param userEmail 비밀번호 재설정을 요청한 사용자의 Email
     * @return 생성된 비밀번호 재설정 토큰
     */
    @Transactional
    public String generatePwResetToken(String userEmail) {
        String token = UUID.randomUUID().toString();
        String key = TOKEN_PREFIX + token;

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, userEmail, Duration.ofHours(24));

        return token;
    }

    /**
     * 비밀번호 재설정 토큰이 유효한지 확인하는 메서드입니다.
     * Redis에서 해당 토큰의 존재 여부를 통해 유효성을 검사합니다.
     *
     * @param token 확인할 비밀번호 재설정 토큰
     * @return 토큰이 유효한 경우 true, 유효하지 않은 경우 false
     */
    public boolean isValidPwResetToken(String token) {
        String key = TOKEN_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 비밀번호 재설정 토큰을 무효화하는 메서드입니다.
     * Redis에서 해당 토큰을 삭제하여 더 이상 사용할 수 없도록 만듭니다.
     *
     * @param token 무효화할 비밀번호 재설정 토큰
     */
    @Transactional
    public void invalidatePwResetToken(String token) {
        String key = TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }
    /* ====================================== 비밀번호 변경 토큰 로직 ====================================== */
}
