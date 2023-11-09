package common.loginapiserver.repository.role;

import common.loginapiserver.entity.Role;
import common.loginapiserver.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
public class CreateTest {
    @Autowired
    EntityManager em;
    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("1. 역할 생성")
    void createTest() {
        Role role = new Role("ROLE_USER");
        roleRepository.save(role);
        Role findRole = roleRepository.findById(role.getId()).get();
        Assertions.assertEquals(role.getId(), findRole.getId());
    }

}
