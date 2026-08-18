package com.example.spring_boot_learning.dto;

public record TodoResponse(
    long id,
    String title,
    boolean completed
) { 
}
