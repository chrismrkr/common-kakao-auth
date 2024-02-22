package common.loginapiserver.member.domain;

import common.loginapiserver.common.utils.PasswordEncoderUtils;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class Member {
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private AuthType authType;

    @Builder
    public Member(Long id, String loginId, String password, String nickname, String email, String phoneNumber, AuthType authType) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.authType = authType;
    }
    public void encodePassword(PasswordEncoderUtils passwordEncoderUtils) {
        String encodedPassword = passwordEncoderUtils.getPasswordEncoder().encode(this.password);
        this.password = encodedPassword;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    public void updateEmail(String email) {
        this.email = email;
    }
}
