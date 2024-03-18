package common.loginapiserver.security.authorization.jwt.domain;

import common.loginapiserver.security.authorization.jwt.infrastructure.entity.OAuthTokenDatabaseEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OAuthTokenTest {
    @Test
    void OAuthToken을_OAuthTokenEntity로_변환할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("abcdef")
                .refreshToken("12345")
                .build();
        // when
        OAuthTokenDatabaseEntity oAuthTokenDatabaseEntity = OAuthTokenDatabaseEntity.from(oAuthToken);
        // then
        Assertions.assertEquals(oAuthTokenDatabaseEntity.getAccessToken(), "abcdef");
        Assertions.assertEquals(oAuthTokenDatabaseEntity.getRefreshToken(), "12345");
    }
    @Test
    void OAuthTokenEntity를_OAuthToken으로_변환할_수_있다() {
        // given
        OAuthTokenDatabaseEntity entity = OAuthTokenDatabaseEntity.builder()
                .accessToken("abcdef")
                .refreshToken("12345").build();
        // when
        OAuthToken domain = entity.to();
        // then
        Assertions.assertEquals(domain.getAccessToken(), "abcdef");
        Assertions.assertEquals(domain.getRefreshToken(), "12345");
    }

}
