package common.loginapiserver.security.authorization.jwt.domain;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.infrastructure.OAuthTokenEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OAuthTokenTest {
    @Test
    void OAuthToken을_OAuthTokenEntity로_변환할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .id(1L)
                .accessToken("abcdef")
                .refreshToken("12345")
                .build();
        // when
        OAuthTokenEntity oAuthTokenEntity = OAuthTokenEntity.from(oAuthToken);
        // then
        Assertions.assertEquals(oAuthTokenEntity.getId(), 1L);
        Assertions.assertEquals(oAuthTokenEntity.getAccessToken(), "abcdef");
        Assertions.assertEquals(oAuthTokenEntity.getRefreshToken(), "12345");
    }
    @Test
    void OAuthTokenEntity를_OAuthToken으로_변환할_수_있다() {
        // given
        OAuthTokenEntity entity = OAuthTokenEntity.builder()
                .id(1L)
                .accessToken("abcdef")
                .refreshToken("12345").build();
        // when
        OAuthToken domain = entity.to();
        // then
        Assertions.assertEquals(domain.getId(), 1L);
        Assertions.assertEquals(domain.getAccessToken(), "abcdef");
        Assertions.assertEquals(domain.getRefreshToken(), "12345");
    }

}
