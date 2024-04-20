package common.loginapiserver.security.authorization.jwt.infrastructure;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.infrastructure.entity.OAuthTokenDatabaseEntity;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Qualifier("oauth_token_rdb_repository")
public class OAuthTokenDatabaseRepositoryImpl implements OAuthTokenRepository {
    private final OAuthTokenJpaRepository oAuthTokenJpaRepository;
    @Override
    public Optional<OAuthToken> findByAccessToken(String accessToken) {
        Optional<OAuthToken> byAccessToken = oAuthTokenJpaRepository.findByAccessToken(accessToken)
                .map(oAuthTokenEntity -> oAuthTokenEntity.to());
        return byAccessToken;
    }
    @Override
    public OAuthToken save(OAuthToken oAuthToken) {
        OAuthToken save = oAuthTokenJpaRepository.save(OAuthTokenDatabaseEntity.from(oAuthToken)).to();
        return save;
    }
    @Override
    public void delete(OAuthToken oAuthToken) {
        oAuthTokenJpaRepository.delete(OAuthTokenDatabaseEntity.from(oAuthToken));
    }
}
