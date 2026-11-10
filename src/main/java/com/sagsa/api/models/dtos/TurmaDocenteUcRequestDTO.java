package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST) do vínculo instrutor x UC x turma
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaDocenteUcRequestDTO {

    @NotNull
    private Long turmaId;

    @NotNull
    private Long unidadeCurricularId;

    @NotNull
    private Long instrutorId;
}
