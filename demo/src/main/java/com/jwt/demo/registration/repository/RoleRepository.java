package com.jwt.demo.registration.repository;

import com.jwt.demo.registration.domain.Role;
import com.jwt.demo.global.constants.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
