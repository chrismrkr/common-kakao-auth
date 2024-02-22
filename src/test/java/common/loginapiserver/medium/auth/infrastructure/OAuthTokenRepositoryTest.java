package common.loginapiserver.medium.auth.infrastructure;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class OAuthTokenRepositoryTest {
    @Autowired
    OAuthTokenRepository oAuthTokenRepository;

    @Test
    void 저장할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("access1")
                .refreshToken("refresh1")
                .build();
        // when
        OAuthToken save = oAuthTokenRepository.save(oAuthToken);
        // then
        Assertions.assertNotNull(save.getId());
        Assertions.assertEquals(save.getAccessToken(), "access1");
        Assertions.assertEquals(save.getRefreshToken(), "refresh1");
    }

    @Test
    void id로_조회할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("access2")
                .refreshToken("refresh2")
                .build();
        OAuthToken save = oAuthTokenRepository.save(oAuthToken);
        // when
        Optional<OAuthToken> byId = oAuthTokenRepository.findById(save.getId());
        // then
        Assertions.assertEquals(byId.get().getId(), save.getId());
        Assertions.assertEquals(byId.get().getAccessToken(), save.getAccessToken());
        Assertions.assertEquals(byId.get().getRefreshToken(), save.getRefreshToken());
    }

    @Test
    void find와_save를_활용해서_수정할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("access3")
                .refreshToken("refresh3")
                .build();
        OAuthToken save = oAuthTokenRepository.save(oAuthToken);

        // when
        OAuthToken find = oAuthTokenRepository.findById(save.getId())
                .orElseThrow(() -> new IllegalArgumentException(""));
        find.updateAccessToken("update-access");
        OAuthToken saved2 = oAuthTokenRepository.save(find);

        // then
        Assertions.assertEquals(save.getId(), saved2.getId());
        Assertions.assertEquals(saved2.getAccessToken(), "update-access");
    }

    @Test
    void accessToken으로_조회할_수_있다() {
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
    void 삭제할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("access5")
                .refreshToken("refresh5")
                .build();
        OAuthToken save = oAuthTokenRepository.save(oAuthToken);

        // when
        OAuthToken find = oAuthTokenRepository.findById(save.getId())
                .get();
        oAuthTokenRepository.delete(find);

        // then
        Assertions.assertEquals(oAuthTokenRepository.findById(save.getId()), Optional.empty());
    }

}
