package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de unidades curriculares
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnidadeCurricularRequestDTO {

    @NotNull
    private Long moduloId;

    @NotBlank
    @Size(max = 150)
    private String nome;

    @NotNull
    @Positive
    private Integer cargaHoraria;

    private String objetivoGeral;

    // Opcional (padrão: 1)
    @Min(1)
    private Integer ordem;
}
