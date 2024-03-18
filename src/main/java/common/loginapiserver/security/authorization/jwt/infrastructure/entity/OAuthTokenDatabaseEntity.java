package common.loginapiserver.security.authorization.jwt.infrastructure.entity;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "o_auth_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthTokenDatabaseEntity {
    @Id
    private String accessToken;
    private String refreshToken;

    @Builder
    public OAuthTokenDatabaseEntity(String accessToken, String refreshToken) {
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
    public static OAuthTokenDatabaseEntity from(OAuthToken oAuthToken) {
        OAuthTokenDatabaseEntity entity = OAuthTokenDatabaseEntity
                .builder()
                .accessToken(oAuthToken.getAccessToken())
                .refreshToken(oAuthToken.getRefreshToken())
                .build();
        return entity;
    }
}
