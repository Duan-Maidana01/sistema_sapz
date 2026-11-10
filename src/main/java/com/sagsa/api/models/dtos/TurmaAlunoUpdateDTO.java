package com.sagsa.api.models.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.StatusMatriculaEnum;

// Entrada (PUT) para alterar a situação da matrícula
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaAlunoUpdateDTO {

    @NotNull
    private StatusMatriculaEnum status;

    // Opcional: só altera quando informada
    private LocalDate dataMatricula;
}
