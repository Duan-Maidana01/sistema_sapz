package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Corpo do POST /login
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {

    private String email;

    private String senha;
}
