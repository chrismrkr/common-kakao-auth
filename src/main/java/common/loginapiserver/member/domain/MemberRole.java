package common.loginapiserver.member.domain;

import common.loginapiserver.role.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRole {
    private Member member;
    private Role role;

    @Builder
    public MemberRole(Member member, Role role) {
        this.member = member;
        this.role = role;
    }
}
