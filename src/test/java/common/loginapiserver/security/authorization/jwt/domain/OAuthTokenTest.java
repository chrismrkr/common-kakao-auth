package common.loginapiserver.security.authorization.jwt.domain;

import common.loginapiserver.security.authorization.jwt.infrastructure.entity.OAuthTokenRedisEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OAuthTokenTest {
    @Test
    void OAuthToken을_OAuthTokenEntity로_변환할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .refreshToken("abcdef")
                .principal("12345")
                .build();
        // when
        OAuthTokenRedisEntity oAuthTokenRedisEntity = OAuthTokenRedisEntity.from(oAuthToken);
        // then
        Assertions.assertEquals(oAuthTokenRedisEntity.getRefreshToken(), "abcdef");
        Assertions.assertEquals(oAuthTokenRedisEntity.getPrincipal(), "12345");
    }
    @Test
    void OAuthTokenEntity를_OAuthToken으로_변환할_수_있다() {
        // given
        OAuthTokenRedisEntity entity = OAuthTokenRedisEntity.builder()
                .refreshToken("abcdef")
                .principal("12345").build();
        // when
        OAuthToken domain = entity.to();
        // then
        Assertions.assertEquals(domain.getRefreshToken(), "abcdef");
        Assertions.assertEquals(domain.getPrincipal(), "12345");
    }

}
