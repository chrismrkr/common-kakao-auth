package common.loginapiserver.service;

import common.loginapiserver.domain.dto.MemberRequestDto;
import common.loginapiserver.domain.entity.Member;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberService {
    Member createMember(MemberRequestDto memberRequestDto);
    UserDetails loadMemberByLoginId(String loginId);
}
