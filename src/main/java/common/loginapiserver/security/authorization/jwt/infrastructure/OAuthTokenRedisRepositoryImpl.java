package common.loginapiserver.security.authorization.jwt.infrastructure;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.infrastructure.entity.OAuthTokenRedisEntity;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Qualifier("oauth_token_redis_repository")
public class OAuthTokenRedisRepositoryImpl implements OAuthTokenRepository {
    private final RedisTemplate redisTemplate;
    @Override
    public Optional<OAuthToken> findByRefreshToken(String refreshToken) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        Object principal = valueOperations.get(refreshToken);
        if(Objects.isNull(principal)) {
            return Optional.empty();
        }

        OAuthToken result = OAuthToken.builder()
                .refreshToken(refreshToken)
                .principal(principal)
                .build();
        return Optional.of(result);
    }

    @Override
    public OAuthToken save(OAuthToken oAuthToken) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        OAuthTokenRedisEntity redisEntity = OAuthTokenRedisEntity.from(oAuthToken);
        valueOperations.set(redisEntity.getRefreshToken(), redisEntity.getPrincipal());

        return redisEntity.to();
    }

    @Override
    public void delete(OAuthToken oAuthToken) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(oAuthToken.getRefreshToken());
    }
}
