package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SapzInstrumentoAvaliacao;
import com.sagsa.api.models.enums.TipoMetodoAvaliacaoEnum;

// Saída (GET) de instrumentos de avaliação da SA
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzInstrumentoAvaliacaoResponseDTO {

    private Long id;
    private Long situacaoAprendizagemId;
    private String situacaoAprendizagemTitulo;
    private String nomeInstrumento;
    private String descricao;
    private TipoMetodoAvaliacaoEnum tipoMetodo;

    public static SapzInstrumentoAvaliacaoResponseDTO fromEntity(SapzInstrumentoAvaliacao obj) {
        return new SapzInstrumentoAvaliacaoResponseDTO(
                obj.getId(),
                obj.getSituacaoAprendizagem().getId(),
                obj.getSituacaoAprendizagem().getTitulo(),
                obj.getNomeInstrumento(),
                obj.getDescricao(),
                obj.getTipoMetodo());
    }
}
