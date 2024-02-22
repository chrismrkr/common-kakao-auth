package common.loginapiserver.security.authorization.jwt.infrastructure;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OAuthTokenRepositoryImpl implements OAuthTokenRepository {
    private final OAuthTokenJpaRepository oAuthTokenJpaRepository;
    @Override
    public Optional<OAuthToken> findByAccessToken(String accessToken) {
        Optional<OAuthToken> byAccessToken = oAuthTokenJpaRepository.findByAccessToken(accessToken)
                .map(oAuthTokenEntity -> oAuthTokenEntity.to());
        return byAccessToken;
    }

    @Override
    public OAuthToken save(OAuthToken oAuthToken) {
        OAuthToken save = oAuthTokenJpaRepository.save(OAuthTokenEntity.from(oAuthToken)).to();
        return save;
    }
    @Override
    public Optional<OAuthToken> findById(Long id) {
        Optional<OAuthToken> byId = oAuthTokenJpaRepository.findById(id)
                .map(oAuthTokenEntity -> oAuthTokenEntity.to());
        return byId;
    }
    @Override
    public void delete(OAuthToken oAuthToken) {
        oAuthTokenJpaRepository.delete(OAuthTokenEntity.from(oAuthToken));
    }
}
