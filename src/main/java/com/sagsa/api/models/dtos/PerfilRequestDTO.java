package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PerfilRequestDTO {

    // Deve ser ADMIN, COORDENADOR ou INSTRUTOR (ver PerfilEnum)
    @NotBlank
    @Size(max = 50)
    private String nome;

    @Size(max = 255)
    private String descricao;
}
