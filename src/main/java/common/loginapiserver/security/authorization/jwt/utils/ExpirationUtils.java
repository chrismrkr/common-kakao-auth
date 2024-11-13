package common.loginapiserver.security.authorization.jwt.utils;

import java.util.Date;

public interface ExpirationUtils {
    long getAccessTokenExpirationMillis();
    long getRefreshTokenExpirationMillis();
    Date now();
}
