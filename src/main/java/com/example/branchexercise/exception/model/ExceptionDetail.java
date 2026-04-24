package com.example.branchexercise.exception.model;

public record ExceptionDetail (
        String type,
        String title,
        int    status,
        String detail,
        String url
)
{}
