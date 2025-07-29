package com.jwt.demo.registration.domain;


import com.jwt.demo.global.constants.ERole;
import com.jwt.demo.global.domain.TransactionInfoDomain;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
public class Role extends TransactionInfoDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {

    }

}
