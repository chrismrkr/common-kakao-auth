package common.loginapiserver.service.impl;

import common.loginapiserver.domain.dto.MemberRequestDto;
import common.loginapiserver.domain.entity.Member;
import common.loginapiserver.domain.entity.MemberRole;
import common.loginapiserver.domain.entity.Role;
import common.loginapiserver.domain.entity.enumerate.AuthType;
import common.loginapiserver.repository.MemberRepository;
import common.loginapiserver.repository.MemberRoleRepository;
import common.loginapiserver.repository.RoleRepository;
import common.loginapiserver.security.provider.MemberContext;
import common.loginapiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;
    @Override
    @Transactional
    public Member createMember(MemberRequestDto memberRequestDto) {
        try {
            Member newMember = Member.builder()
                    .loginId(memberRequestDto.getLoginId())
                    .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                    .email(memberRequestDto.getEmail())
                    .nickname(memberRequestDto.getNickname())
                    .phoneNumber(memberRequestDto.getPhoneNumber())
                    .authType(AuthType.valueOf(memberRequestDto.getAuthType()))
                    .build();
            Member savedMember = memberRepository.save(newMember);
            Role role = getMemberRole("ROLE_USER");
            MemberRole memberRole = new MemberRole(savedMember, role);
            memberRoleRepository.save(memberRole);

            return savedMember;
        } catch(Exception e) {
            // Exception 발생 시 트랜잭션 롤백을 어떻게 진행할 것인가?
            throw e;
        }
    }

    @Override
    public UserDetails loadMemberByLoginId(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("UsernameNotFoundException");
                });
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
    private Role getMemberRole(String authority) {
        Role role = roleRepository.findByRoleName(authority).orElseGet(() -> {
            Role newRole = new Role(authority);
            Role save = roleRepository.save(newRole);
            return save;
        });
        return role;
    }
}
