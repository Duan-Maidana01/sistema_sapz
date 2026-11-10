package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de recursos e ambientes pedagógicos da SA
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzRecursoAmbienteRequestDTO {

    @NotNull
    private Long situacaoAprendizagemId;

    @NotBlank
    @Size(max = 150)
    private String ambientePedagogico;

    @NotBlank
    private String recursosDidaticos;
}
