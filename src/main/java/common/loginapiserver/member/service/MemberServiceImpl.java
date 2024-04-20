package common.loginapiserver.member.service;

import common.loginapiserver.common.utils.PasswordEncoderUtils;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.service.port.MemberRepository;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.domain.Role;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import common.loginapiserver.role.service.port.RoleRepository;
import common.loginapiserver.member.domain.MemberContext;
import common.loginapiserver.member.controller.port.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderUtils passwordEncoderUtils;

    @Override
    @Transactional
    public MemberRole createMember(Member member) {
        try {
            if(!isValidMemberCreate(member)) {
                throw new IllegalArgumentException("Login Id is Duplicated!");
            }
            member.encodePassword(passwordEncoderUtils);
            Member savedMember = memberRepository.save(member);
            Role role = getRole("ROLE_USER");
            MemberRole memberRole = memberRoleRepository.link(savedMember, role);
            return memberRole;
        } catch(Exception e) {
            // Exception 발생 시 트랜잭션 롤백을 어떻게 진행할 것인가?
            throw e;
        }
    }

    @Override
    public UserDetails loadMemberByLoginId(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.getByLoginId(loginId);
        if(!member.getAuthType().equals(AuthType.NORMAL)) {
            throw new IllegalArgumentException("AuthType must be NORMAL!");
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        List<MemberRole> byLoginId = memberRoleRepository.findByLoginId(loginId);
        byLoginId.forEach(memberRole -> {
            roles.add(new SimpleGrantedAuthority(memberRole.getRole().getRoleName()));
        });
        MemberContext memberContext = new MemberContext(member, roles);
        return memberContext;
    }
    private Role getRole(String authority) {
        Role roleEntity = roleRepository.getByRoleName(authority);
        return roleEntity;
    }

    private boolean isValidMemberCreate(Member member) {
        return memberRepository.findByLoginId(member.getLoginId())
                .isEmpty() ? true : false;
    }
}
