package com.jwt.demo.registration.domain;

import com.jwt.demo.global.domain.TransactionInfoDomain;
import jakarta.persistence.*;
import lombok.Data;

@Data
//@Entity
@Table(name = "user_roles")
public class UserRole extends TransactionInfoDomain {

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
