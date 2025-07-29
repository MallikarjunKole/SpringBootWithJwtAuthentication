package com.jwt.demo.registration.dto;

import lombok.Data;

@Data
public class RegistrationResponse {

    private String timestamp;
    private int status;
    private Exception error;
    private String message;
    private String token;
    private Object response;

}
