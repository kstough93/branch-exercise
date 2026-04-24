package com.example.branchexercise.controller;

import com.example.branchexercise.exception.UserNotFoundException;
import com.example.branchexercise.exception.model.ExceptionDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDetail> handleNotFound(
            UserNotFoundException e, HttpServletRequest request) {
        logger.error("User not found exception.");
        return ResponseEntity.status(404).body(new ExceptionDetail(
                "https://localhost/errors/internal-error",
                "User Not Found",
                404,
                e.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionDetail> handleNoUserName(
            NoResourceFoundException e, HttpServletRequest request) {
        logger.error("Username path variable is required.");
        return ResponseEntity.status(422).body(new ExceptionDetail(
                "https://localhost/errors/internal-error",
                "Username path variable is required.",
                422,
                e.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetail> handleUnexpected(
            Exception e, HttpServletRequest request) {
        logger.error("Unexpected error at {}", request.getRequestURI(), e);
        return ResponseEntity.status(500).body(new ExceptionDetail(
                "https://localhost/errors/internal-error",
                "Internal Server Error",
                500,
                "An unexpected error occurred. Reference: username",
                request.getRequestURI()
        ));
    }
}
