package common.loginapiserver.member.controller.port;

import common.loginapiserver.member.controller.dto.MemberRequestDto;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberService {
    MemberRole createMember(Member member);
    UserDetails loadMemberByLoginId(String loginId);
}
