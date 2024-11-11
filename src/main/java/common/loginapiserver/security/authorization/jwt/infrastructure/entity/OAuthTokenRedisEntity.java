package common.loginapiserver.security.authorization.jwt.infrastructure.entity;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class OAuthTokenRedisEntity {
    @Id
    private String refreshToken;
    private Object principal;
    @Builder
    public OAuthTokenRedisEntity(String refreshToken, Object principal) {
        this.refreshToken = refreshToken;
        this.principal = principal;
    }
    public OAuthToken to() {
        OAuthToken domain = OAuthToken.builder()
                .refreshToken(this.refreshToken)
                .principal(this.principal)
                .build();
        return domain;
    }
    public static OAuthTokenRedisEntity from(OAuthToken oAuthToken) {
        OAuthTokenRedisEntity entity = OAuthTokenRedisEntity
                .builder()
                .refreshToken(oAuthToken.getRefreshToken())
                .principal(oAuthToken.getPrincipal())
                .build();
        return entity;
    }
}
