package common.loginapiserver.security.oauth2;

import common.loginapiserver.security.oauth2.jwt.OAuthTokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthTokenInfoRepository extends JpaRepository<OAuthTokenInfo, Long> {
    Optional<OAuthTokenInfo> findByAccessToken(String accessToken);
}
