package common.loginapiserver.repository.MemberRole;

import common.loginapiserver.entity.Member;
import common.loginapiserver.entity.MemberRole;
import common.loginapiserver.entity.Role;
import common.loginapiserver.repository.MemberRepository;
import common.loginapiserver.repository.MemberRoleRepository;
import common.loginapiserver.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
public class CreateTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRoleRepository memberRoleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("1. Member - Role 매핑")
    void createMapping() {
        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        Member member = Member.builder()
                .loginId("kangok")
                .password("1234")
                .nickname("303")
                .email("email@email.com")
                .phoneNumber("010000000000")
                .build();
        memberRepository.save(member);

        MemberRole memberRole1 = new MemberRole(member, roleUser);
        MemberRole memberRole2 = new MemberRole(member, roleAdmin);
        memberRoleRepository.save(memberRole1);
        memberRoleRepository.save(memberRole2);

        Member findMember = memberRepository.findById(member.getId()).get();
        List<MemberRole> memberRoleList = findMember.getMemberRoleList();
        Assertions.assertEquals(memberRoleList.size(), 2);
    }
}
