package common.loginapiserver.repository.MemberRole;

import common.loginapiserver.entity.Member;
import common.loginapiserver.entity.MemberRole;
import common.loginapiserver.entity.Role;
import common.loginapiserver.repository.MemberRepository;
import common.loginapiserver.repository.MemberRoleRepository;
import common.loginapiserver.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
public class SelectTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRoleRepository memberRoleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("1. member.loginId 조회")
    @Transactional
    void selectByMemberLoginId() {
        // given
        Member a = Member.builder().loginId("a").build();
        Member b = Member.builder().loginId("b").build();
        Member saveA = memberRepository.save(a);
        Member saveB = memberRepository.save(b);

        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleManager = new Role("ROLE_MANAGER");
        Role saveRoleUser = roleRepository.save(roleUser);
        Role saveRoleAdmin = roleRepository.save(roleAdmin);
        Role saveRoleManager = roleRepository.save(roleManager);

        MemberRole memberRole1 = new MemberRole(saveA, saveRoleUser);
        MemberRole memberRole2 = new MemberRole(saveA, saveRoleAdmin);
        MemberRole memberRole3 = new MemberRole(saveB, saveRoleManager);
        MemberRole memberRole4 = new MemberRole(saveB, saveRoleAdmin);
        memberRoleRepository.save(memberRole1);
        memberRoleRepository.save(memberRole2);
        memberRoleRepository.save(memberRole3);
        memberRoleRepository.save(memberRole4);
        em.flush();
        em.clear();

        // when
        memberRoleRepository.findByLoginId(a.getLoginId());
    }
}
