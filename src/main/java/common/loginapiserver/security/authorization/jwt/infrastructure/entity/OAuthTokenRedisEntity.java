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
    private String accessToken;
    private String refreshToken;
    @Builder
    public OAuthTokenRedisEntity(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public OAuthToken to() {
        OAuthToken domain = OAuthToken.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .build();
        return domain;
    }
    public static OAuthTokenRedisEntity from(OAuthToken oAuthToken) {
        OAuthTokenRedisEntity entity = OAuthTokenRedisEntity
                .builder()
                .accessToken(oAuthToken.getAccessToken())
                .refreshToken(oAuthToken.getRefreshToken())
                .build();
        return entity;
    }
}
