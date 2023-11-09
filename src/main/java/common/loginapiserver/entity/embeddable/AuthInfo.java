package common.loginapiserver.entity.embeddable;

import common.loginapiserver.entity.enumerate.AuthType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Map;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInfo {
    @Transient
    private Map<String, Object> authorities;
    public AuthInfo(Map<String, Object> authorities, String authType) {
        this.authorities = authorities;
    }
}
