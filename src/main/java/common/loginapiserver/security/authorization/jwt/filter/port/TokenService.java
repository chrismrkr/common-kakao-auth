package common.loginapiserver.security.authorization.jwt.filter.port;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;

public interface TokenService {
    OAuthToken saveOAuthToken(String refreshToken, Object principal) throws InterruptedException;
    OAuthToken findOAuthToken(String refreshToken) throws InterruptedException;
    void deleteOAuthToken(OAuthToken oAuthToken) throws InterruptedException;
}
