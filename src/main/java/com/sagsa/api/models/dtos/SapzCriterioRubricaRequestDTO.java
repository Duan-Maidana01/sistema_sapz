package com.sagsa.api.models.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de rubricas (níveis) dos critérios
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzCriterioRubricaRequestDTO {

    @NotNull
    private Long criterioId;

    @NotNull
    @Min(1)
    private Integer nivel;

    @NotBlank
    private String descricaoRubrica;

    // Opcional (padrão: 0.00)
    @DecimalMin(value = "0.00")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal pontuacao;
}
