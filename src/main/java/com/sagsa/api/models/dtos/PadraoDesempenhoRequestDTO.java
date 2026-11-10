package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de padrões de desempenho
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PadraoDesempenhoRequestDTO {

    @NotNull
    private Long subfuncaoId;

    @NotBlank
    private String descricao;
}
