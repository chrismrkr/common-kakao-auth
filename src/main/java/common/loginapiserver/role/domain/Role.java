package common.loginapiserver.role.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Role {
    private Long id;

    private String roleName;

    @Builder
    public Role(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
