package common.loginapiserver.member.infrastructure.entity;

import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.role.infrastructure.entity.RoleEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(MemberRoleEntity.JoinTableId.class)
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member_role")
public class MemberRoleEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;
    public MemberRoleEntity(MemberEntity memberEntity, RoleEntity roleEntity) {
        this.memberEntity = memberEntity;
        this.roleEntity = roleEntity;
    }
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JoinTableId implements Serializable {
        private Long memberEntity;
        private Long roleEntity;
        public static class Builder {
            private Long memberEntity;
            private Long roleEntity;
            public Builder memberPK(Long id) {
                this.memberEntity = id;
                return this;
            }
            public Builder rolePK(Long id) {
                this.roleEntity = id;
                return this;
            }
            public JoinTableId build() {
                return new JoinTableId(this);
            }
        }
        private JoinTableId(Builder builder) {
            this.memberEntity = builder.memberEntity;
            this.roleEntity = builder.roleEntity;
        }
        public static Builder builder() {
            return new Builder();
        }
    }

    public static MemberRoleEntity from(MemberRole memberRole) {
        return new MemberRoleEntity(MemberEntity.from(memberRole.getMember()),
                                    RoleEntity.from(memberRole.getRole()));
    }
    public MemberRole to() {
        MemberRole memberRole = MemberRole.builder()
                .member(this.getMemberEntity().to())
                .role(this.getRoleEntity().to())
                .build();
        return memberRole;
    }

}
