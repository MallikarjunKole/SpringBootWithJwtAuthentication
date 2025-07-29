package com.jwt.demo.registration.dto;


import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {
    private Long id;
    private String token;
    private String username;
    private String email;
    private List<String> roles;

    public UserInfoResponse(Long id, String token, String username, String email, List<String> roles) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}

