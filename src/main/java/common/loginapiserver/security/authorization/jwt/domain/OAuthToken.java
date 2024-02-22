package common.loginapiserver.security.authorization.jwt.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthToken {
    private Long id;
    private String accessToken;
    private String refreshToken;

    @Builder
    public OAuthToken(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
