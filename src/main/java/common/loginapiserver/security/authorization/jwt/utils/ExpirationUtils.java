package common.loginapiserver.security.authorization.jwt.utils;

import java.util.Date;

public interface ExpirationUtils {
    long getAccessTokenExpiration();
    long getRefreshTokenExpiration();
    Date now();
}
