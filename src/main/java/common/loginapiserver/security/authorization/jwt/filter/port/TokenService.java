package common.loginapiserver.security.authorization.jwt.filter.port;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;

public interface TokenService {
    void saveOAuthToken(String accessToken, String refreshToken);
    OAuthToken findOAuthToken(String accessToken);
    void deleteOAuthToken(OAuthToken oAuthToken);
    OAuthToken updateAccessToken(OAuthToken oAuthToken, String newAccessToken);
    OAuthToken updateAccessToken(Long id, String newAccessToken);

}
