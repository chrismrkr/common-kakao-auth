package common.loginapiserver.role.infrastructure;

import common.loginapiserver.role.domain.Role;
import common.loginapiserver.role.infrastructure.entity.RoleEntity;
import common.loginapiserver.role.service.port.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Role getByRoleName(String authority) {
        Role role = roleJpaRepository.findByRoleName(authority).orElseGet(() -> {
            RoleEntity newRoleEntity = roleJpaRepository.save(new RoleEntity(authority));
            return newRoleEntity;
        }).to();
        return role;
    }

    @Override
    public Role save(Role role) {
        Role ret = roleJpaRepository.save(RoleEntity.from(role)).to();
        return ret;
    }
}
