package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SapzEstrategiaEnsino;

// Saída (GET) de estratégias de ensino da SA
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzEstrategiaEnsinoResponseDTO {

    private Long id;
    private Long situacaoAprendizagemId;
    private String situacaoAprendizagemTitulo;
    private String nomeEstrategia;
    private String descricaoAtividade;

    public static SapzEstrategiaEnsinoResponseDTO fromEntity(SapzEstrategiaEnsino obj) {
        return new SapzEstrategiaEnsinoResponseDTO(
                obj.getId(),
                obj.getSituacaoAprendizagem().getId(),
                obj.getSituacaoAprendizagem().getTitulo(),
                obj.getNomeEstrategia(),
                obj.getDescricaoAtividade());
    }
}
