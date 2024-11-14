package common.loginapiserver.security.authorization.jwt.service;

import common.loginapiserver.common.MockExpirationUtils;
import common.loginapiserver.common.MockOAuthTokenRepository;
import common.loginapiserver.security.authorization.jwt.filter.port.TokenService;
import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.OAuthTokenService;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import common.loginapiserver.security.authorization.jwt.utils.ExpirationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OAuthTokenServiceTest {
    static OAuthTokenRepository tokenRepository;
    static TokenService tokenService;
    static ExpirationUtils expirationUtils;
    @BeforeAll
    static void init() {
        tokenRepository = new MockOAuthTokenRepository();
        expirationUtils = new MockExpirationUtils(100, 100);
        tokenService = new OAuthTokenService(expirationUtils, tokenRepository);
    }
    @Test
    void accessToken과_refreshToken을_전달받아_OAuthToken을_저장한다() throws InterruptedException {
        // given
        String accessToken = "access1";
        String refreshToken = "refresh1";
        Object principal = "principal1";
        // when
        tokenService.saveOAuthToken(refreshToken, principal);
        // then
        Assertions.assertEquals(tokenRepository.findByRefreshToken(refreshToken).get().getPrincipal(), principal);
    }
    @Test
    void accessToken으로_oAuthToken을_조회할_수_있다() throws InterruptedException {
        // given
        String accessToken = "access2";
        String refreshToken = "refresh2";
        Object principal = "principal2";
        tokenService.saveOAuthToken(refreshToken, principal);
        // when
        OAuthToken find = tokenService.findOAuthToken(refreshToken);
        // then
        Assertions.assertEquals(find.getRefreshToken(), refreshToken);
        Assertions.assertEquals(find.getPrincipal(), principal);
    }
    @Test
    void 존재하지_않는_accessToken이면_Exception이_발생한다() throws InterruptedException {
        // given
        String accessToken = "access3";
        String refreshToken = "refresh3";
        Object principal = "principal3";
        tokenService.saveOAuthToken(refreshToken, principal);
        // when
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> tokenService.findOAuthToken("access-no"));
    }
    @Test
    void oAuthToken을_삭제할_수_있다() throws InterruptedException {
        // given
        String accessToken = "access4";
        String refreshToken = "refresh4";
        Object principal = "principal4";
        tokenService.saveOAuthToken(refreshToken, principal);
        Assertions.assertDoesNotThrow(() -> tokenService.findOAuthToken(refreshToken));
        // when
        OAuthToken oAuthToken = tokenService.findOAuthToken(refreshToken);
        tokenService.deleteOAuthToken(oAuthToken);
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> tokenService.findOAuthToken(refreshToken));
    }
}
