package com.jwt.demo.registration.service;

import com.jwt.demo.registration.domain.User;

import java.util.Optional;

public interface RegistrationService {

    Optional<User> findByUsername(String username);
}
