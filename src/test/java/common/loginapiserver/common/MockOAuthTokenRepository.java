package common.loginapiserver.common;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MockOAuthTokenRepository implements OAuthTokenRepository {
    private static AtomicLong atomicLong = new AtomicLong();
    private List<OAuthToken> datas = new ArrayList<>();
    @Override
    public Optional<OAuthToken> findByAccessToken(String accessToken) {
        Optional<OAuthToken> any = datas.stream().filter(token -> token.getAccessToken().equals(accessToken)).findAny();
        return any;
    }

    @Override
    public OAuthToken save(OAuthToken oAuthToken) {
        if(datas.size() > 0) {
            Optional<OAuthToken> any = datas.stream().filter(token -> token.getId() == oAuthToken.getId()).findAny();
            if(!any.isEmpty()) {
                datas.remove(any);
            }
        }
        OAuthToken newOAuthToken = OAuthToken.builder()
                .id(atomicLong.getAndIncrement())
                .accessToken(oAuthToken.getAccessToken())
                .refreshToken(oAuthToken.getRefreshToken())
                .build();
        datas.add(newOAuthToken);
        return newOAuthToken;
    }

    @Override
    public Optional<OAuthToken> findById(Long id) {
        Optional<OAuthToken> any = datas.stream().filter(token -> token.getId() == id).findAny();
        return any;
    }

    @Override
    public void delete(OAuthToken oAuthToken) {
        datas.remove(oAuthToken);
    }
}
