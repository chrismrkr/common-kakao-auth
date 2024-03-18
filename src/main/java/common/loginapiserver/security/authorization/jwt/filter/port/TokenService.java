package common.loginapiserver.security.authorization.jwt.filter.port;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;

public interface TokenService {
    OAuthToken saveOAuthToken(String accessToken, String refreshToken) throws InterruptedException;
    OAuthToken findOAuthToken(String accessToken) throws InterruptedException;
    void deleteOAuthToken(OAuthToken oAuthToken) throws InterruptedException;
}
