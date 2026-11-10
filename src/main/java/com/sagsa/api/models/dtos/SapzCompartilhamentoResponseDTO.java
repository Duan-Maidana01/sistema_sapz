package com.sagsa.api.models.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SapzCompartilhamento;
import com.sagsa.api.models.enums.TipoCompartilhamentoEnum;

// Saída (GET) de compartilhamento
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzCompartilhamentoResponseDTO {

    private Long id;
    private Long situacaoAprendizagemId;
    private String situacaoAprendizagemTitulo;
    private TipoCompartilhamentoEnum tipoCompartilhamento;
    private Long escolaId;
    private String escolaNome;
    private Long cursoId;
    private String cursoNome;
    private Long usuarioDestinatarioId;
    private String usuarioDestinatarioNome;
    private LocalDateTime dataCompartilhamento;

    public static SapzCompartilhamentoResponseDTO fromEntity(SapzCompartilhamento obj) {
        return new SapzCompartilhamentoResponseDTO(
                obj.getId(),
                obj.getSituacaoAprendizagem().getId(),
                obj.getSituacaoAprendizagem().getTitulo(),
                obj.getTipoCompartilhamento(),
                Objects.nonNull(obj.getEscola()) ? obj.getEscola().getId() : null,
                Objects.nonNull(obj.getEscola()) ? obj.getEscola().getNome() : null,
                Objects.nonNull(obj.getCurso()) ? obj.getCurso().getId() : null,
                Objects.nonNull(obj.getCurso()) ? obj.getCurso().getNome() : null,
                Objects.nonNull(obj.getUsuarioDestinatario()) ? obj.getUsuarioDestinatario().getId() : null,
                Objects.nonNull(obj.getUsuarioDestinatario()) ? obj.getUsuarioDestinatario().getNome() : null,
                obj.getDataCompartilhamento());
    }
}
