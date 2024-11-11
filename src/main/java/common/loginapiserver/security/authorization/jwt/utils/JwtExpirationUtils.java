package common.loginapiserver.security.authorization.jwt.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
public class JwtExpirationUtils implements ExpirationUtils {
    @Value("${oauth2.jwt.access-token-expiration}")
    private String accessTokenExpiration;
    @Value("${oauth2.jwt.refresh-token-expiration}")
    private String refreshTokenExpiration;

    @Override
    public long getAccessTokenExpiration() {
        return Long.parseLong(accessTokenExpiration) * 1000L;  // 30 min
    }
    @Override
    public long getRefreshTokenExpiration() {
        return Long.parseLong(refreshTokenExpiration) * 1000L; // 7 days
    }
    @Override
    public Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
}
