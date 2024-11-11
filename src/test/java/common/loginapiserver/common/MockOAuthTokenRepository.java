package common.loginapiserver.common;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MockOAuthTokenRepository implements OAuthTokenRepository {
    private List<OAuthToken> datas = new ArrayList<>();
    @Override
    public Optional<OAuthToken> findByRefreshToken(String refreshToken) {
        Optional<OAuthToken> any = datas.stream().filter(token -> token.getRefreshToken().equals(refreshToken)).findAny();
        return any;
    }

    @Override
    public OAuthToken save(OAuthToken oAuthToken) {
        if(datas.size() > 0) {
            Optional<OAuthToken> any = datas.stream().filter(token -> token.getRefreshToken().equals(oAuthToken.getRefreshToken())).findAny();
            if(!any.isEmpty()) {
                datas.remove(any);
            }
        }
        OAuthToken newOAuthToken = OAuthToken.builder()
                .refreshToken(oAuthToken.getRefreshToken())
                .principal(oAuthToken.getPrincipal())
                .build();
        datas.add(newOAuthToken);
        return newOAuthToken;
    }

    @Override
    public void delete(OAuthToken oAuthToken) {
        datas.remove(oAuthToken);
    }
}
