package common.loginapiserver.security.authorization.jwt.domain;

import lombok.Getter;

@Getter
public class MemberJwtDetails {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpirationTime;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
    public static class Builder {
        private String grantType;
        private String accessToken;
        private Long accessTokenExpirationTime;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
        public Builder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }
        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
        public Builder accessTokenExpirationTime(Long accessTokenExpirationTime) {
            this.accessTokenExpirationTime = accessTokenExpirationTime;
            return this;
        }
        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        public Builder refreshTokenExpirationTime(Long refreshTokenExpirationTime) {
            this.refreshTokenExpirationTime = refreshTokenExpirationTime;
            return this;
        }
        public MemberJwtDetails build() {
            return new MemberJwtDetails(this);
        }
    }
    public static Builder builder() {
        return new Builder();
    }
    private MemberJwtDetails(Builder builder) {
        this.grantType = builder.grantType;
        this.accessToken = builder.accessToken;
        this.accessTokenExpirationTime = builder.accessTokenExpirationTime;
        this.refreshToken = builder.refreshToken;
        this.refreshTokenExpirationTime = builder.refreshTokenExpirationTime;
    }
}
