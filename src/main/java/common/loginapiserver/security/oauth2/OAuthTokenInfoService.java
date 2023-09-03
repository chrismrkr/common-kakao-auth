package common.loginapiserver.security.oauth2;

import common.loginapiserver.security.oauth2.jwt.OAuthTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthTokenInfoService {
    private final OAuthTokenInfoRepository oAuthTokenInfoRepository;
    public void saveOAuthTokenInfo(String accessToken, String refreshToken) {
        oAuthTokenInfoRepository.save(new OAuthTokenInfo(accessToken, refreshToken));
    }
    public OAuthTokenInfo findOAuthTokenInfo(String accessToken) {
        OAuthTokenInfo oAuthTokenInfo = oAuthTokenInfoRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("INVALID ACCESS TOKEN: REFRESH TOKEN NOT FOUND"));
        return oAuthTokenInfo;
    }

    public void deleteOAuthTokenInfo(OAuthTokenInfo oAuthTokenInfo) {
        oAuthTokenInfoRepository.delete(oAuthTokenInfo);
    }
    @Transactional
    public OAuthTokenInfo updateAccessToken(Long id, String newAccessToken) {
        OAuthTokenInfo oAuthTokenInfo = oAuthTokenInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OAUTH TOKEN INFO NOT FOUND"));
        oAuthTokenInfo.updateAccessToken(newAccessToken);
        return oAuthTokenInfo;
    }
    public OAuthTokenInfo updateAccessToken(OAuthTokenInfo oAuthTokenInfo, String newAccessToken) {
        oAuthTokenInfo.updateAccessToken(newAccessToken);
        return oAuthTokenInfo;
    }

}
