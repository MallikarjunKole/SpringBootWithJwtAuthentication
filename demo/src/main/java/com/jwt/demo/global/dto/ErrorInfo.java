package com.jwt.demo.global.dto;

public record ErrorInfo(
        int status,
        String error,
        String errorMessage,
        String errorCode
) {
}
