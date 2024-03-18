package common.loginapiserver.medium.auth.infrastructure;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class OAuthTokenDatabaseRepositoryTest {
    OAuthTokenRepository oAuthTokenRepository;
    OAuthTokenDatabaseRepositoryTest(@Qualifier("oauth_token_db_repository") OAuthTokenRepository oAuthTokenRepository) {
        this.oAuthTokenRepository = oAuthTokenRepository;
    }

    @Test
    void 저장할_수_있다() throws InterruptedException {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("access1")
                .refreshToken("refresh1")
                .build();
        // when
        OAuthToken save = oAuthTokenRepository.save(oAuthToken);
        // then
        Assertions.assertEquals(save.getAccessToken(), "access1");
        Assertions.assertEquals(save.getRefreshToken(), "refresh1");
    }

    @Test
    void accessToken으로_조회할_수_있다() throws InterruptedException {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("access4")
                .refreshToken("refresh4")
                .build();
        OAuthToken save = oAuthTokenRepository.save(oAuthToken);

        // when
        Optional<OAuthToken> access4 = oAuthTokenRepository.findByAccessToken("access4");

        // then
        Assertions.assertEquals(access4.get().getAccessToken(), "access4");
    }
    @Test
    void 삭제할_수_있다() throws InterruptedException {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("access5")
                .refreshToken("refresh5")
                .build();
        OAuthToken save = oAuthTokenRepository.save(oAuthToken);

        // when
        OAuthToken find = oAuthTokenRepository.findByAccessToken(save.getAccessToken())
                .get();
        oAuthTokenRepository.delete(find);

        // then
        Assertions.assertEquals(oAuthTokenRepository.findByAccessToken(save.getAccessToken()),
                Optional.empty());
    }

}
