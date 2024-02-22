package common.loginapiserver.role.infrastructure.entity;

import common.loginapiserver.member.infrastructure.entity.MemberRoleEntity;
import common.loginapiserver.role.domain.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    private String roleName;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roleEntity", cascade = CascadeType.REMOVE)
    private List<MemberRoleEntity> memberRoleEntityList = new ArrayList<>();
    public RoleEntity(String roleName) {
        this.roleName = roleName;
    }
    public RoleEntity(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
    protected RoleEntity() {}
    public static RoleEntity from(Role role) {
        RoleEntity roleEntity = new RoleEntity(role.getId(), role.getRoleName());
        return roleEntity;
    }
    public Role to() {
        Role role = Role.builder()
                .id(this.id)
                .roleName(this.roleName)
                .build();
        return role;
    }
}
