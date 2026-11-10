package com.sagsa.api.models.dtos;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Turma;

// Saída (GET) de turmas
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaResponseDTO {

    private Long id;
    private Long escolaId;
    private String escolaNome;
    private Long planoCursoId;
    private String planoCursoCodigo;
    private Long cursoId;
    private String cursoNome;
    private Long coordenadorId;
    private String coordenadorNome;
    private String codigoTurma;
    private Integer ano;
    private Integer semestre;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean ativo;

    public static TurmaResponseDTO fromEntity(Turma obj) {
        return new TurmaResponseDTO(
                obj.getId(),
                obj.getEscola().getId(),
                obj.getEscola().getNome(),
                obj.getPlanoCurso().getId(),
                obj.getPlanoCurso().getCodigoPlano(),
                obj.getPlanoCurso().getCurso().getId(),
                obj.getPlanoCurso().getCurso().getNome(),
                Objects.nonNull(obj.getCoordenador()) ? obj.getCoordenador().getId() : null,
                Objects.nonNull(obj.getCoordenador()) ? obj.getCoordenador().getNome() : null,
                obj.getCodigoTurma(),
                obj.getAno(),
                obj.getSemestre(),
                obj.getDataInicio(),
                obj.getDataFim(),
                obj.getAtivo());
    }
}
