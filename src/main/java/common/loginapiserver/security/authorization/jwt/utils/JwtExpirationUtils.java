package common.loginapiserver.security.authorization.jwt.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
public class JwtExpirationUtils implements ExpirationUtils {

    @Override
    public long getAccessTokenExpiration() {
        return 30 * 60 * 1000L;  // 30 min
    }
    @Override
    public long getRefreshTokenExpiration() {
        return 7 * 24 * 60 * 1000L; // 7 days
    }
    @Override
    public Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
}
