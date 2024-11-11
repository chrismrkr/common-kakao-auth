package common.loginapiserver.security.authorization.jwt.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthToken {
    private String refreshToken;
    private Object principal;

    @Builder
    public OAuthToken(String refreshToken, Object principal) {
        this.refreshToken = refreshToken;
        this.principal = principal;
    }
}
