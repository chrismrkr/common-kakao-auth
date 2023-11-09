package common.loginapiserver.service.impl;

import common.loginapiserver.repository.MemberRoleRepository;
import common.loginapiserver.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl {
    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;
}
