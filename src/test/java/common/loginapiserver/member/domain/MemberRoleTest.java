package common.loginapiserver.member.domain;

import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import common.loginapiserver.member.infrastructure.entity.MemberRoleEntity;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import common.loginapiserver.role.domain.Role;
import common.loginapiserver.role.infrastructure.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberRoleTest {
    
    @Test
    void MemberRole를_MemberRoleEntity로_변환할_수_있다() {
        // given
        Member member = Member.builder()
                .id(1L)
                .loginId("kang00")
                .password("1234")
                .nickname("ok")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        Role role = Role.builder().id(1L)
                .roleName("ROLE_USER")
                .build();
        MemberRole memberRole = MemberRole.builder()
                .member(member)
                .role(role)
                .build();
        // when
        MemberRoleEntity memberRoleEntity = MemberRoleEntity.from(memberRole);
        // then
        Assertions.assertEquals(member.getId(), memberRoleEntity.getMemberEntity().getId());
        Assertions.assertEquals(member.getLoginId(), memberRoleEntity.getMemberEntity().getLoginId());
        Assertions.assertEquals(member.getPassword(), memberRoleEntity.getMemberEntity().getPassword());
        Assertions.assertEquals(member.getNickname(), memberRoleEntity.getMemberEntity().getNickname());
        Assertions.assertEquals(member.getEmail(), memberRoleEntity.getMemberEntity().getEmail());
        Assertions.assertEquals(member.getPhoneNumber(), memberRoleEntity.getMemberEntity().getPhoneNumber());
        Assertions.assertEquals(member.getAuthType(), memberRoleEntity.getMemberEntity().getAuthType());

        Assertions.assertEquals(role.getId(), memberRoleEntity.getRoleEntity().getId());
        Assertions.assertEquals(role.getRoleName(), memberRoleEntity.getRoleEntity().getRoleName());
    }
    
    @Test
    void MemberRoleEntity를_MemberRole로_변환_할_수_있다() {
        // given
        MemberEntity memberEntity = MemberEntity.builder()
                .id(1L)
                .loginId("kang00")
                .password("1234")
                .nickname("ok")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        RoleEntity roleEntity = new RoleEntity(1L, "ROLE_USER");
        MemberRoleEntity memberRoleEntity = new MemberRoleEntity(memberEntity, roleEntity);

        // when
        MemberRole memberRole = memberRoleEntity.to();

        // then
        Assertions.assertEquals(memberEntity.getId(), memberRole.getMember().getId());
        Assertions.assertEquals(memberEntity.getLoginId(), memberRole.getMember().getLoginId());
        Assertions.assertEquals(memberEntity.getPassword(), memberRole.getMember().getPassword());
        Assertions.assertEquals(memberEntity.getNickname(), memberRole.getMember().getNickname());
        Assertions.assertEquals(memberEntity.getEmail(), memberRole.getMember().getEmail());
        Assertions.assertEquals(memberEntity.getPhoneNumber(), memberRole.getMember().getPhoneNumber());
        Assertions.assertEquals(memberEntity.getAuthType(), memberRole.getMember().getAuthType());

        Assertions.assertEquals(roleEntity.getId(), memberRole.getRole().getId());
        Assertions.assertEquals(roleEntity.getRoleName(), memberRole.getRole().getRoleName());
    }
}
