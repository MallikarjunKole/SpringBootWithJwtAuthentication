package com.jwt.demo.registration.serviceImpl;

import com.jwt.demo.registration.domain.User;
import com.jwt.demo.registration.repository.UserRepository;
import com.jwt.demo.registration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
