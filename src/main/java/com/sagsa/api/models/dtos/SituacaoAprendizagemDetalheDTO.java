package com.sagsa.api.models.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Visão completa de uma SA (base para a geração do PDF):
// dados gerais + capacidades + conhecimentos + estratégias + recursos + instrumentos -> critérios -> rubricas
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SituacaoAprendizagemDetalheDTO {

    private SituacaoAprendizagemResponseDTO situacaoAprendizagem;
    private List<CapacidadeResponseDTO> capacidades;
    private List<ConhecimentoResponseDTO> conhecimentos;
    private List<SapzEstrategiaEnsinoResponseDTO> estrategiasEnsino;
    private List<SapzRecursoAmbienteResponseDTO> recursosAmbientes;
    private List<InstrumentoDetalhe> instrumentosAvaliacao;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class InstrumentoDetalhe {
        private SapzInstrumentoAvaliacaoResponseDTO instrumento;
        private List<CriterioDetalhe> criterios;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class CriterioDetalhe {
        private SapzCriterioAvaliacaoResponseDTO criterio;
        private List<SapzCriterioRubricaResponseDTO> rubricas;
    }
}
