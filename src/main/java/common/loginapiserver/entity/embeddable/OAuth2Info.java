package common.loginapiserver.entity.embeddable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Map;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Info {
    private Long oAuth2Id;
    private String oAuth2Type;
    @Transient
    private Map<String, Object> attributes;
    public OAuth2Info(Map<String, Object> attributes, String oAuth2Type) {
        this.attributes = attributes;
        this.oAuth2Id = (Long)attributes.get("id");
        this.oAuth2Type = oAuth2Type;
    }
    public Long getOAuth2Id() {
        return this.oAuth2Id;
    }
    public String getEmail() {
        if(this.oAuth2Type.equals("KAKAO")) {
            return (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
        }
        else return null;
    }

    public String getNickname() {
        return (String)((Map<String, Object>)attributes.get("properties")).get("nickname");
    }
}
