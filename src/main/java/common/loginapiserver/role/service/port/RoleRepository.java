package common.loginapiserver.role.service.port;

import common.loginapiserver.role.domain.Role;
import common.loginapiserver.role.infrastructure.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository {

    Role getByRoleName(String authority);

    Role save(Role role);
}
