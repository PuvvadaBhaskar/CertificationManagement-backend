package com.project.dto;

import lombok.Data;

@Data
public class UserRequestDto {

    private String name;
    private String email;
    private String role;
    private String password;  
}