package common.loginapiserver.entity;

import common.loginapiserver.entity.embeddable.OAuth2Info;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @Embedded
    private OAuth2Info oAuth2Info;

    public void updateOAuth2Info(OAuth2Info oAuth2Info) {
        this.oAuth2Info = oAuth2Info;
    }
    public static class Builder {
        private OAuth2Info oAuth2Info;
        public Builder oAuth2Info(OAuth2Info oAuth2Info) {
            this.oAuth2Info = oAuth2Info;
            return this;
        }
        public Member build() {
            return new Member(this);
        }
    }
    public static Builder builder() {
        return new Builder();
    }
    private Member(Builder builder) {
        this.oAuth2Info = builder.oAuth2Info;
    }
}
