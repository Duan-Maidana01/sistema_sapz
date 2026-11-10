package com.sagsa.api.models.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de turmas
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaRequestDTO {

    @NotNull
    private Long escolaId;

    @NotNull
    private Long planoCursoId;

    // Opcional: se um COORDENADOR criar a turma sem informar, ele mesmo será o coordenador
    private Long coordenadorId;

    @NotBlank
    @Size(max = 50)
    private String codigoTurma;

    @NotNull
    @Min(2000)
    @Max(2100)
    private Integer ano;

    @NotNull
    @Min(1)
    @Max(2)
    private Integer semestre;

    @NotNull
    private LocalDate dataInicio;

    @NotNull
    private LocalDate dataFim;

    // Opcional (padrão: true)
    private Boolean ativo;
}
