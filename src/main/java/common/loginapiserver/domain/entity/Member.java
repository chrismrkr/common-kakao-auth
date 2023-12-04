package common.loginapiserver.domain.entity;

import common.loginapiserver.domain.entity.enumerate.AuthType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private AuthType authType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    public void updateEmail(String email) {
        this.email = email;
    }

    public static class Builder {
        private String loginId;
        private String password;
        private String nickname;
        private String email;
        private String phoneNumber;
        private AuthType authType;

        public Builder loginId(String loginId) {
            this.loginId = loginId;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public Builder authType(AuthType authType) {
            this.authType = authType;
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
        this.loginId = builder.loginId;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.authType = builder.authType;
    }
}