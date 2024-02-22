package common.loginapiserver.member.domain;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MemberContext extends User {
    private final Member member;
    public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getLoginId(), member.getPassword(), authorities);
        this.member = member;
    }
    public Member getMember() {
        return this.member;
    }
}
