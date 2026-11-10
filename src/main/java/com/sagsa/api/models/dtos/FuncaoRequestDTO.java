package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de funções do perfil profissional
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FuncaoRequestDTO {

    @NotNull
    private Long planoCursoId;

    @NotNull
    @Min(1)
    private Integer numero;

    @NotBlank
    private String descricao;
}
