package common.loginapiserver.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(MemberRole.JoinTableId.class)
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberRole {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    public MemberRole(Member member, Role role) {
        this.member = member;
        this.role = role;
    }
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JoinTableId implements Serializable {
        private Long member;
        private Long role;
        public static class Builder {
            private Long member;
            private Long role;
            public Builder memberPK(Long id) {
                this.member = id;
                return this;
            }
            public Builder rolePK(Long id) {
                this.role = id;
                return this;
            }
            public JoinTableId build() {
                return new JoinTableId(this);
            }
        }
        private JoinTableId(Builder builder) {
            this.member = builder.member;
            this.role = builder.role;
        }
        public static Builder builder() {
            return new Builder();
        }
    }
}
