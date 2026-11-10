package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SapzRecursoAmbiente;

// Saída (GET) de recursos e ambientes pedagógicos da SA
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzRecursoAmbienteResponseDTO {

    private Long id;
    private Long situacaoAprendizagemId;
    private String situacaoAprendizagemTitulo;
    private String ambientePedagogico;
    private String recursosDidaticos;

    public static SapzRecursoAmbienteResponseDTO fromEntity(SapzRecursoAmbiente obj) {
        return new SapzRecursoAmbienteResponseDTO(
                obj.getId(),
                obj.getSituacaoAprendizagem().getId(),
                obj.getSituacaoAprendizagem().getTitulo(),
                obj.getAmbientePedagogico(),
                obj.getRecursosDidaticos());
    }
}
