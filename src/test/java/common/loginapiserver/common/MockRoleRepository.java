package common.loginapiserver.common;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.role.domain.Role;
import common.loginapiserver.role.service.port.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MockRoleRepository implements RoleRepository {
    private static AtomicLong atomicLong = new AtomicLong();
    private List<Role> datas = new ArrayList<>();

    @Override
    public Role getByRoleName(String authority) {
        Optional<Role> role = datas.stream().filter(r -> r.getRoleName().equals(authority))
                .findAny();
        if(role.isEmpty()) {
            Role newRole = Role.builder()
                    .roleName(authority)
                    .build();
            Role saveRole = save(newRole);
            return saveRole;
        }
        return role.get();
    }

    @Override
    public Role save(Role role) {
        if(datas.size() > 0) {
            Optional<Role> any = datas.stream().filter(r -> r.getId() == role.getId()).findAny();
            if(!any.isEmpty()) {
                datas.remove(any.get());
            }
        }
        Role r = Role.builder()
                .id(atomicLong.getAndIncrement())
                .roleName(role.getRoleName())
                .build();
        datas.add(r);
        return r;
    }
}
