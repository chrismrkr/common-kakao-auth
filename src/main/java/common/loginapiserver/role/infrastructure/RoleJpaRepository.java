package common.loginapiserver.role.infrastructure;

import common.loginapiserver.role.infrastructure.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    @Query(value = "SELECT r FROM RoleEntity r " +
                    "WHERE r.roleName = :roleName")
    Optional<RoleEntity> findByRoleName(@Param("roleName")String roleName);
}
