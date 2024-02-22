package common.loginapiserver.member.service.port;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.infrastructure.entity.MemberRoleEntity;
import common.loginapiserver.role.domain.Role;

import java.util.List;

public interface MemberRoleRepository {
    List<MemberRole> findByLoginId(String loginId);

    MemberRole link(Member member, Role role);
    void delete(MemberRole memberRole);
}
