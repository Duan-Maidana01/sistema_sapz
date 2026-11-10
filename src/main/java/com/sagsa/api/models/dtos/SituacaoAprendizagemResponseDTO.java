package com.sagsa.api.models.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SituacaoAprendizagem;
import com.sagsa.api.models.enums.TipoEstrategiaEnum;

// Saída (GET) de Situação de Aprendizagem
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SituacaoAprendizagemResponseDTO {

    private Long id;
    private String titulo;
    private Long unidadeCurricularId;
    private String unidadeCurricularNome;
    private Long instrutorId;
    private String instrutorNome;
    private Long turmaId;
    private String turmaCodigo;
    private TipoEstrategiaEnum tipoEstrategia;
    private String contextualizacao;
    private String desafio;
    private String resultadosEsperados;
    private Integer cargaHorariaSa;
    private Boolean compartilhada;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<Long> capacidadeIds;
    private List<Long> conhecimentoIds;

    public static SituacaoAprendizagemResponseDTO fromEntity(SituacaoAprendizagem obj) {
        return new SituacaoAprendizagemResponseDTO(
                obj.getId(),
                obj.getTitulo(),
                obj.getUnidadeCurricular().getId(),
                obj.getUnidadeCurricular().getNome(),
                obj.getInstrutor().getId(),
                obj.getInstrutor().getNome(),
                Objects.nonNull(obj.getTurma()) ? obj.getTurma().getId() : null,
                Objects.nonNull(obj.getTurma()) ? obj.getTurma().getCodigoTurma() : null,
                obj.getTipoEstrategia(),
                obj.getContextualizacao(),
                obj.getDesafio(),
                obj.getResultadosEsperados(),
                obj.getCargaHorariaSa(),
                obj.getCompartilhada(),
                obj.getDataCriacao(),
                obj.getDataAtualizacao(),
                obj.getCapacidades().stream().map(c -> c.getId()).sorted().toList(),
                obj.getConhecimentos().stream().map(c -> c.getId()).sorted().toList());
    }
}
