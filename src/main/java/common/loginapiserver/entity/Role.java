package common.loginapiserver.entity;

import lombok.*;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    private String roleName;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.REMOVE)
    private List<MemberRole> memberRoleList = new ArrayList<>();
    public Role(String roleName) {
        this.roleName = roleName;
    }
    protected Role() {}
}
