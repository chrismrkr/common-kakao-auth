package common.loginapiserver.security.authorization.jwt.infrastructure;

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
public class OAuthTokenEntity {
    @Id
    @GeneratedValue
    @Column(name = "oauth_token_id")
    private Long id;
    private String accessToken;
    private String refreshToken;
    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    @Builder
    public OAuthTokenEntity(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public OAuthToken to() {
        OAuthToken domain = OAuthToken.builder()
                .id(this.id)
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .build();
        return domain;
    }
    public static OAuthTokenEntity from(OAuthToken oAuthToken) {
        OAuthTokenEntity entity = OAuthTokenEntity
                .builder()
                .id(oAuthToken.getId())
                .accessToken(oAuthToken.getAccessToken())
                .refreshToken(oAuthToken.getRefreshToken())
                .build();
        return entity;
    }
}
