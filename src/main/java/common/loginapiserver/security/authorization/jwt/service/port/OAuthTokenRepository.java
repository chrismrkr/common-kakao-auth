package common.loginapiserver.security.authorization.jwt.service.port;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;

import java.util.Optional;

public interface OAuthTokenRepository {
    Optional<OAuthToken> findByAccessToken(String accessToken) throws InterruptedException;
    OAuthToken save(OAuthToken oAuthToken) throws InterruptedException;
    void delete(OAuthToken oAuthToken) throws InterruptedException;
}
