package com.sagsa.api.models.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.PlanoCurso;

// Saída (GET) de planos de curso
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanoCursoResponseDTO {

    private Long id;
    private Long cursoId;
    private String cursoNome;
    private String codigoPlano;
    private String versao;
    private Integer cargaHorariaTotal;
    private LocalDate dataAprovacao;
    private Boolean ativo;

    public static PlanoCursoResponseDTO fromEntity(PlanoCurso obj) {
        return new PlanoCursoResponseDTO(
                obj.getId(),
                obj.getCurso().getId(),
                obj.getCurso().getNome(),
                obj.getCodigoPlano(),
                obj.getVersao(),
                obj.getCargaHorariaTotal(),
                obj.getDataAprovacao(),
                obj.getAtivo());
    }
}
