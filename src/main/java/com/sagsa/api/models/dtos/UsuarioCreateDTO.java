package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioCreateDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 60)
    private String senha;

    @NotNull
    private Long perfilId;

    // Opcional (padrão: true)
    private Boolean ativo;
}
