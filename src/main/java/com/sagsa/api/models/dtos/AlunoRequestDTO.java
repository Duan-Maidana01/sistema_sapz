package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de alunos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlunoRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 14)
    @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}", message = "CPF inválido (use 000.000.000-00)")
    private String cpf;

    @Size(max = 150)
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    private String matricula;
}
