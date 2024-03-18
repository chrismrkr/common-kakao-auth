package common.loginapiserver.security.authorization.jwt.service;

import common.loginapiserver.common.MockOAuthTokenRepository;
import common.loginapiserver.security.authorization.jwt.filter.port.TokenService;
import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.OAuthTokenService;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OAuthTokenServiceTest {
    static OAuthTokenRepository tokenRepository;
    static TokenService tokenService;

    @BeforeAll
    static void init() {
        tokenRepository = new MockOAuthTokenRepository();
        tokenService = new OAuthTokenService(tokenRepository);
    }
    @Test
    void accessToken과_refreshToken을_전달받아_OAuthToken을_저장한다() throws InterruptedException {
        // given
        String accessToken = "access1";
        String refreshToken = "refresh1";
        // when
        tokenService.saveOAuthToken(accessToken, refreshToken);
        // then
        Assertions.assertEquals(tokenRepository.findByAccessToken("access1").get().getAccessToken(), "access1");
        Assertions.assertEquals(tokenRepository.findByAccessToken("access1").get().getRefreshToken(), "refresh1");
    }
    @Test
    void accessToken으로_oAuthToken을_조회할_수_있다() throws InterruptedException {
        // given
        String accessToken = "access2";
        String refreshToken = "refresh2";
        tokenService.saveOAuthToken(accessToken, refreshToken);
        // when
        OAuthToken find = tokenService.findOAuthToken("access2");
        // then
        Assertions.assertEquals(find.getAccessToken(), accessToken);
        Assertions.assertEquals(find.getRefreshToken(), refreshToken);
    }
    @Test
    void 존재하지_않는_accessToken이면_Exception이_발생한다() throws InterruptedException {
        // given
        String accessToken = "access3";
        String refreshToken = "refresh3";
        tokenService.saveOAuthToken(accessToken, refreshToken);
        // when
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> tokenService.findOAuthToken("access-no"));
    }
    @Test
    void oAuthToken을_삭제할_수_있다() throws InterruptedException {
        // given
        String accessToken = "access4";
        String refreshToken = "refresh4";
        tokenService.saveOAuthToken(accessToken, refreshToken);
        Assertions.assertDoesNotThrow(() -> tokenService.findOAuthToken(accessToken));
        // when
        OAuthToken oAuthToken = tokenService.findOAuthToken(accessToken);
        tokenService.deleteOAuthToken(oAuthToken);
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> tokenService.findOAuthToken(accessToken));
    }
}
