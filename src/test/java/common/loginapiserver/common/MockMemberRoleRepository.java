package common.loginapiserver.common;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.domain.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MockMemberRoleRepository implements MemberRoleRepository {
    private List<MemberRole> datas = new ArrayList<>();

    @Override
    public List<MemberRole> findByLoginId(String loginId) {
        List<MemberRole> memberRoleList = datas.stream().filter(memberRole -> memberRole.getMember().getLoginId().equals(loginId))
                .toList();
        return memberRoleList;
    }

    @Override
    public MemberRole link(Member member, Role role) {
        MemberRole memberRole = MemberRole.builder()
                .member(member)
                .role(role)
                .build();
        datas.add(memberRole);
        return memberRole;
    }

    @Override
    public void delete(MemberRole memberRole) {
        MemberRole find = datas.stream().filter(mr -> mr.getMember().getId() == memberRole.getMember().getId() && mr.getRole().getId() == memberRole.getRole().getId())
                .findAny().orElseGet(() -> {
                    throw new RuntimeException();
                });
        datas.remove(find);
    }
}
