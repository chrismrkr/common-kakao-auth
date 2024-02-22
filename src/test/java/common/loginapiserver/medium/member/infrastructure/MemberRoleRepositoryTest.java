package common.loginapiserver.medium.member.infrastructure;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import common.loginapiserver.member.service.port.MemberRepository;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.domain.Role;
import common.loginapiserver.role.service.port.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
public class MemberRoleRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRoleRepository memberRoleRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    @Transactional
    void Member와_Role은_N대N_매핑할_수_있다() {
        // given
        Member member1 = Member.builder()
                .loginId("member1")
                .password("1234")
                .nickname("ok")
                .email("member1@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        Member member2 = Member.builder()
                .loginId("member2")
                .password("1234")
                .nickname("ok")
                .email("member2@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        Role role1 = Role.builder().roleName("ROLE_USER").build();
        Role role2 = Role.builder().roleName("ROLE_ADMIN").build();

        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Role savedRole1 = roleRepository.save(role1);
        Role savedRole2 = roleRepository.save(role2);

        // when
        MemberRole memberRole1 = memberRoleRepository.link(savedMember1, savedRole1);
        MemberRole memberRole2 = memberRoleRepository.link(savedMember1, savedRole2);
        MemberRole memberRole3 = memberRoleRepository.link(savedMember2, savedRole1);
        MemberRole memberRole4 = memberRoleRepository.link(savedMember2, savedRole2);

        // then
        Assertions.assertEquals(memberRole1.getMember().getId(), savedMember1.getId());
        Assertions.assertEquals(memberRole2.getMember().getId(), savedMember1.getId());
        Assertions.assertEquals(memberRole3.getMember().getId(), savedMember2.getId());
        Assertions.assertEquals(memberRole4.getMember().getId(), savedMember2.getId());
        Assertions.assertEquals(memberRole1.getRole().getId(), savedRole1.getId());
        Assertions.assertEquals(memberRole2.getRole().getId(), savedRole2.getId());
        Assertions.assertEquals(memberRole3.getRole().getId(), savedRole1.getId());
        Assertions.assertEquals(memberRole4.getRole().getId(), savedRole2.getId());
    }

    @Test
    @Transactional
    void MemberRole을_loginId로_조회할_수_있다() {
        // given
        Member member = Member.builder()
                .loginId("member3")
                .password("1234")
                .nickname("ok")
                .email("member1@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        Role role1 = Role.builder().roleName("ROLE_USER").build();
        Role role2 = Role.builder().roleName("ROLE_ADMIN").build();
        member = memberRepository.save(member);
        role1 = roleRepository.save(role1);
        role2 = roleRepository.save(role2);

        // when
        memberRoleRepository.link(member, role1);
        memberRoleRepository.link(member, role2);
        List<MemberRole> memberRoleList = memberRoleRepository.findByLoginId("member");

        // then
        Assertions.assertEquals(2, memberRoleList.size());
    }

    @Test
    @Transactional
    void MemberRole을_삭제할_수_있다() {
        // given
        Member member = Member.builder()
                .loginId("member4")
                .password("1234")
                .nickname("ok")
                .email("member1@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        Role role1 = Role.builder().roleName("ROLE_USER").build();
        Role role2 = Role.builder().roleName("ROLE_ADMIN").build();
        member = memberRepository.save(member);
        role1 = roleRepository.save(role1);
        role2 = roleRepository.save(role2);

        MemberRole memberRole1 = memberRoleRepository.link(member, role1);
        MemberRole memberRole2 = memberRoleRepository.link(member, role2);

        // when
        memberRoleRepository.delete(memberRole1);
        memberRoleRepository.delete(memberRole2);

        // then
        Assertions.assertEquals(memberRoleRepository.findByLoginId("member4").size(), 0);
    }
}