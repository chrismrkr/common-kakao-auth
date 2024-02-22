package common.loginapiserver.security.authorization.jwt.service;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.filter.port.TokenService;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthTokenService implements TokenService {
    private final OAuthTokenRepository oAuthTokenRepository;
    @Override
    public void saveOAuthToken(String accessToken, String refreshToken) {
        oAuthTokenRepository.save(
                OAuthToken.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }
    @Override
    public OAuthToken findOAuthToken(String accessToken) {
        OAuthToken oAuthToken = oAuthTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("INVALID ACCESS TOKEN: REFRESH TOKEN NOT FOUND"));
        return oAuthToken;
    }
    @Override
    public void deleteOAuthToken(OAuthToken oAuthToken) {
        oAuthTokenRepository.delete(oAuthToken);
    }
    @Override
    public OAuthToken updateAccessToken(Long id, String newAccessToken) {
        OAuthToken oAuthToken = oAuthTokenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OAUTH TOKEN INFO NOT FOUND"));
        oAuthToken.updateAccessToken(newAccessToken);
        OAuthToken newOAuthToken = oAuthTokenRepository.save(oAuthToken);
        return newOAuthToken;
    }
    @Override
    public OAuthToken updateAccessToken(OAuthToken oAuthToken, String newAccessToken) {
        oAuthToken.updateAccessToken(newAccessToken);
        OAuthToken newOAuthToken = oAuthTokenRepository.save(oAuthToken);
        return newOAuthToken;
    }
}
