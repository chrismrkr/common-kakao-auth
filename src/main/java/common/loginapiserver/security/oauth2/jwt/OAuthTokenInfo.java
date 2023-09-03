package common.loginapiserver.security.oauth2.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthTokenInfo {
    @Id
    @GeneratedValue
    @Column(name = "oauth_token_info_id")
    private Long id;
    private String accessToken;
    private String refreshToken;
    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public OAuthTokenInfo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
