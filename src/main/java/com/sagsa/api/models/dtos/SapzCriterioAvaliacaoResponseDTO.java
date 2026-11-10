package com.sagsa.api.models.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SapzCriterioAvaliacao;

// Saída (GET) de critérios de avaliação
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzCriterioAvaliacaoResponseDTO {

    private Long id;
    private Long instrumentoId;
    private String instrumentoNomeInstrumento;
    private Long capacidadeId;
    private String capacidadeDescricao;
    private String descricaoCriterio;
    private BigDecimal prioridadePeso;

    public static SapzCriterioAvaliacaoResponseDTO fromEntity(SapzCriterioAvaliacao obj) {
        return new SapzCriterioAvaliacaoResponseDTO(
                obj.getId(),
                obj.getInstrumento().getId(),
                obj.getInstrumento().getNomeInstrumento(),
                obj.getCapacidade().getId(),
                obj.getCapacidade().getDescricao(),
                obj.getDescricaoCriterio(),
                obj.getPrioridadePeso());
    }
}
