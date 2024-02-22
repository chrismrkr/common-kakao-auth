package common.loginapiserver.common;

import common.loginapiserver.member.service.port.MemberRepository;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.service.port.RoleRepository;
import lombok.Builder;

public class TestContainer {
    public MemberRepository memberRepository;
    public MemberRoleRepository memberRoleRepository;
    public RoleRepository roleRepository;
    @Builder
    public TestContainer(MemberRepository memberRepository, MemberRoleRepository memberRoleRepository, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.memberRoleRepository = memberRoleRepository;
        this.roleRepository = roleRepository;
    }
}
