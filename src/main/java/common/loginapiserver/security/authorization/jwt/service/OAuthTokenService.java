package common.loginapiserver.security.authorization.jwt.service;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.filter.port.TokenService;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import common.loginapiserver.security.authorization.jwt.utils.ExpirationUtils;
import common.loginapiserver.security.authorization.jwt.utils.JwtExpirationUtils;
import common.loginapiserver.security.authorization.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuthTokenService implements TokenService {
    private final ExpirationUtils expirationUtils;
    private final OAuthTokenRepository oAuthTokenRepository;
    public OAuthTokenService(ExpirationUtils expirationUtils, @Qualifier("oauth_token_redis_repository") OAuthTokenRepository oAuthTokenRepository) {
        this.expirationUtils = expirationUtils;
        this.oAuthTokenRepository = oAuthTokenRepository;
    }
    @Override
    public OAuthToken saveOAuthToken(String refreshToken, Object principal) throws InterruptedException {
        OAuthToken save = oAuthTokenRepository.save(
                OAuthToken.builder()
                        .refreshToken(refreshToken)
                        .principal(principal)
                        .build(),
                expirationUtils.getRefreshTokenExpirationMillis() / 60000L
        );
        return save;
    }
    @Override
    public OAuthToken findOAuthToken(String refreshToken) throws InterruptedException {
        OAuthToken oAuthToken = oAuthTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("INVALID TOKEN: REFRESH TOKEN NOT FOUND"));
        return oAuthToken;
    }
    @Override
    public void deleteOAuthToken(OAuthToken oAuthToken) throws InterruptedException {
        oAuthTokenRepository.delete(oAuthToken);
    }
}
