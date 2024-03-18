package common.loginapiserver.security.authorization.jwt.infrastructure;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.infrastructure.entity.OAuthTokenRedisEntity;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OAuthTokenRedisRepositoryImpl implements OAuthTokenRepository {
    private final RedisTemplate redisTemplate;
    @Override
    public Optional<OAuthToken> findByAccessToken(String accessToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String refreshToken = valueOperations.get(accessToken);
        if(Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        OAuthToken result = OAuthToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return Optional.of(result);
    }

    @Override
    public OAuthToken save(OAuthToken oAuthToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        OAuthTokenRedisEntity redisEntity = OAuthTokenRedisEntity.from(oAuthToken);
        valueOperations.set(redisEntity.getAccessToken(), redisEntity.getRefreshToken());

        return redisEntity.to();
    }

    @Override
    public void delete(OAuthToken oAuthToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(oAuthToken.getAccessToken());
    }
}
