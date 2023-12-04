package common.loginapiserver.security.provider;

import common.loginapiserver.domain.entity.Member;
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
