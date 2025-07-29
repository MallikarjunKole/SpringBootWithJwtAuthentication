package com.jwt.demo.registration.dto;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;
    private String email;
    private String password;

}
