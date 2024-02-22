package common.loginapiserver.member.infrastructure.entity;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "member")
public class MemberEntity {
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

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE)
    private List<MemberRoleEntity> memberRoleEntityList = new ArrayList<>();

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    public void updateEmail(String email) {
        this.email = email;
    }

    public static class Builder {
        private Long id;
        private String loginId;
        private String password;
        private String nickname;
        private String email;
        private String phoneNumber;
        private AuthType authType;
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public MemberEntity build() {
            return new MemberEntity(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private MemberEntity(Builder builder) {
        this.id = builder.id;
        this.loginId = builder.loginId;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.authType = builder.authType;
    }
    public static MemberEntity from(Member member) {
        MemberEntity memberEntity = MemberEntity.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .authType(member.getAuthType())
                .build();
        return memberEntity;
    }
    public Member to() {
        Member member = Member.builder()
                .id(this.id)
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .authType(this.authType)
                .build();
        return member;
    }
}