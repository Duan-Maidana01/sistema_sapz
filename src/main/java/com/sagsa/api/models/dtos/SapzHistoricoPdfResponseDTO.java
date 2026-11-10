package com.sagsa.api.models.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SapzHistoricoPdf;

// Saída (GET) do histórico de PDFs
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzHistoricoPdfResponseDTO {

    private Long id;
    private Long situacaoAprendizagemId;
    private String situacaoAprendizagemTitulo;
    private String versao;
    private String caminhoArquivoPdf;
    private String hashConteudo;
    private LocalDateTime dataGeracao;
    private Long criadoPorId;
    private String criadoPorNome;

    public static SapzHistoricoPdfResponseDTO fromEntity(SapzHistoricoPdf obj) {
        return new SapzHistoricoPdfResponseDTO(
                obj.getId(),
                obj.getSituacaoAprendizagem().getId(),
                obj.getSituacaoAprendizagem().getTitulo(),
                obj.getVersao(),
                obj.getCaminhoArquivoPdf(),
                obj.getHashConteudo(),
                obj.getDataGeracao(),
                obj.getCriadoPor().getId(),
                obj.getCriadoPor().getNome());
    }
}
