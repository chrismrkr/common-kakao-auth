package common.loginapiserver.security.authorization.jwt.service;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.filter.port.TokenService;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OAuthTokenService implements TokenService {
    private final OAuthTokenRepository oAuthTokenRepository;
    public OAuthTokenService(@Qualifier("oauth_token_db_repository") OAuthTokenRepository oAuthTokenRepository) {
        this.oAuthTokenRepository = oAuthTokenRepository;
    }
    @Override
    public OAuthToken saveOAuthToken(String accessToken, String refreshToken) throws InterruptedException {
        OAuthToken save = oAuthTokenRepository.save(
                OAuthToken.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
        return save;
    }
    @Override
    public OAuthToken findOAuthToken(String accessToken) throws InterruptedException {
        OAuthToken oAuthToken = oAuthTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("INVALID ACCESS TOKEN: REFRESH TOKEN NOT FOUND"));
        return oAuthToken;
    }
    @Override
    public void deleteOAuthToken(OAuthToken oAuthToken) throws InterruptedException {
        oAuthTokenRepository.delete(oAuthToken);
    }
}
