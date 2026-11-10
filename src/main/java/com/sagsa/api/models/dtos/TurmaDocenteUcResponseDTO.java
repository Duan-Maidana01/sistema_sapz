package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.TurmaDocenteUc;

// Saída (GET) do vínculo instrutor x UC x turma
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaDocenteUcResponseDTO {

    private Long turmaId;
    private String turmaCodigo;
    private Long unidadeCurricularId;
    private String unidadeCurricularNome;
    private Long instrutorId;
    private String instrutorNome;

    public static TurmaDocenteUcResponseDTO fromEntity(TurmaDocenteUc obj) {
        return new TurmaDocenteUcResponseDTO(
                obj.getTurma().getId(),
                obj.getTurma().getCodigoTurma(),
                obj.getUnidadeCurricular().getId(),
                obj.getUnidadeCurricular().getNome(),
                obj.getInstrutor().getId(),
                obj.getInstrutor().getNome());
    }
}
