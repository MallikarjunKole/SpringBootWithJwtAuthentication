package com.jwt.demo.global.dto;

import java.time.LocalDateTime;

public record Response<T>(
        boolean flag,
        String message,
        T data,
        ErrorInfo error,
        LocalDateTime timestamp

) {

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(true, message, data, null, LocalDateTime.now());
    }

    public static <T> Response<T> failure(String message, ErrorInfo error) {
        return new Response<>(false, message, null, error, LocalDateTime.now());
    }
}
