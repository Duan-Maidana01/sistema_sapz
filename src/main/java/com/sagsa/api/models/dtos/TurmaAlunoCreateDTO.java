package com.sagsa.api.models.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.StatusMatriculaEnum;

// Entrada (POST) de matrícula de aluno em turma
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaAlunoCreateDTO {

    @NotNull
    private Long turmaId;

    @NotNull
    private Long alunoId;

    // Opcional (padrão: data de hoje)
    private LocalDate dataMatricula;

    // Opcional (padrão: MATRICULADO)
    private StatusMatriculaEnum status;
}
