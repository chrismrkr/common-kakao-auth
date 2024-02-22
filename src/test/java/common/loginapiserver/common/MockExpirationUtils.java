package common.loginapiserver.common;

import common.loginapiserver.security.authorization.jwt.utils.ExpirationUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MockExpirationUtils implements ExpirationUtils {
    private long accessTokenExpirationTime;
    private long refreshTokenExpirationTime;
    public MockExpirationUtils(long accessTokenExpirationTime, long refreshTokenExpirationTime) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }
    @Override
    public long getAccessTokenExpiration() {
        // 1 sec
        return accessTokenExpirationTime;
    }
    @Override
    public long getRefreshTokenExpiration() {
        // 2 sec
        return refreshTokenExpirationTime;
    }
    @Override
    public Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
}
