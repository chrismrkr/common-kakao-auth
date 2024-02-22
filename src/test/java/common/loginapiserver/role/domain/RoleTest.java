package common.loginapiserver.role.domain;

import common.loginapiserver.role.infrastructure.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTest {
    @Test
    void Role에서_RoleEntity로_변환할_수_있다() {
        // given
        Role role = Role.builder()
                .id(1L)
                .roleName("ROLE_USER")
                .build();
        // when
        RoleEntity roleEntity = RoleEntity.from(role);

        // then
        Assertions.assertEquals(role.getId(), roleEntity.getId());
        Assertions.assertEquals(role.getRoleName(), roleEntity.getRoleName());

    }
    @Test
    void RoleEntity에서_Role로_변환할_수_있다() {
        // given
        RoleEntity roleEntity = new RoleEntity(1L, "ROLE_USER");
        // when
        Role role = roleEntity.to();
        // then
        Assertions.assertEquals(role.getId(), roleEntity.getId());
        Assertions.assertEquals(role.getRoleName(), roleEntity.getRoleName());
    }
}
