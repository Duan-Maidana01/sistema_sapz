package com.sagsa.api.models.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de critérios de avaliação
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzCriterioAvaliacaoRequestDTO {

    @NotNull
    private Long instrumentoId;

    @NotNull
    private Long capacidadeId;

    @NotBlank
    private String descricaoCriterio;

    // Opcional (padrão: 1.00)
    @DecimalMin(value = "0.01")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal prioridadePeso;
}
