package common.loginapiserver.member.infrastructure;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.infrastructure.entity.MemberRoleEntity;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class MemberRoleRepositoryImpl implements MemberRoleRepository {
    private final MemberRoleJpaRepository memberRoleJpaRepository;
    @Override
    public List<MemberRole> findByLoginId(String loginId) {
        List<MemberRole> memberRoleList = memberRoleJpaRepository.findByLoginId(loginId)
                .stream().map(memberRoleEntity -> memberRoleEntity.to()).toList();
        return memberRoleList;
    }

    @Override
    public MemberRole link(Member member, Role role) {
        MemberRoleEntity memberRoleEntity = MemberRoleEntity.from(
                MemberRole.builder()
                        .member(member)
                        .role(role)
                        .build()
        );
        MemberRoleEntity entity = memberRoleJpaRepository.save(memberRoleEntity);
        return entity.to();
    }

    @Override
    public void delete(MemberRole memberRole) {
        MemberRoleEntity memberRoleEntity = MemberRoleEntity.from(memberRole);
        memberRoleJpaRepository.delete(memberRoleEntity);
    }
}
