package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de estratégias de ensino da SA
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzEstrategiaEnsinoRequestDTO {

    @NotNull
    private Long situacaoAprendizagemId;

    @NotBlank
    @Size(max = 100)
    private String nomeEstrategia;

    private String descricaoAtividade;
}
