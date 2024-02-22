package common.loginapiserver.security.authorization.jwt.service.port;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;

import java.util.Optional;

public interface OAuthTokenRepository {
    Optional<OAuthToken> findByAccessToken(String accessToken);
    OAuthToken save(OAuthToken oAuthToken);
    Optional<OAuthToken> findById(Long id);
    void delete(OAuthToken oAuthToken);
}
