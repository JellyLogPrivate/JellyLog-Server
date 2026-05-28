package com.saram.jellylog.global.exception;

import com.saram.jellylog.global.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus status = determineStatus(e.getMessage());
        return ResponseEntity.status(status).body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ApiResponse.error("요청 값이 올바르지 않습니다.");
    }

    private HttpStatus determineStatus(String message) {
        if (message == null) {
            return HttpStatus.BAD_REQUEST;
        }

        String normalized = message.toLowerCase();
        if (normalized.contains("jwt") || normalized.contains("refresh token") || normalized.contains("토큰")) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.BAD_REQUEST;
    }
}

