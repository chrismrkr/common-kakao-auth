package common.loginapiserver.security.authorization.jwt.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
public class JwtExpirationUtils implements ExpirationUtils {
    @Value("${oauth2.jwt.access-token-expiration}")
    private String accessTokenMinuteExpiration;
    @Value("${oauth2.jwt.refresh-token-expiration}")
    private String refreshTokenMinuteExpiration;

    @Override
    public long getAccessTokenExpirationMillis() {
        return Long.parseLong(accessTokenMinuteExpiration) * 60 * 1000L;  // millis
    }
    @Override
    public long getRefreshTokenExpirationMillis() {
        return Long.parseLong(refreshTokenMinuteExpiration) * 60 * 1000L; // millis
    }
    @Override
    public Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
}
