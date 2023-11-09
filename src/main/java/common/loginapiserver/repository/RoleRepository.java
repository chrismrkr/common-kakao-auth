package common.loginapiserver.repository;

import common.loginapiserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT r FROM Role r " +
                    "WHERE r.roleName = :roleName")
    Optional<Role> findByRoleName(@Param("roleName")String roleName);
}
