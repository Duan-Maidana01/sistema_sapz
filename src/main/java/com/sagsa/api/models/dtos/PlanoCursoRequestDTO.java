package com.sagsa.api.models.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de planos de curso
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanoCursoRequestDTO {

    @NotNull
    private Long cursoId;

    @NotBlank
    @Size(max = 50)
    private String codigoPlano;

    // Opcional (padrão: 1.0)
    @Size(max = 20)
    private String versao;

    @NotNull
    @Positive
    private Integer cargaHorariaTotal;

    private LocalDate dataAprovacao;

    // Opcional (padrão: true)
    private Boolean ativo;
}
