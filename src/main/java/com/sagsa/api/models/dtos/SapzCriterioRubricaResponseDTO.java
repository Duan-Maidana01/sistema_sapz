package com.sagsa.api.models.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.SapzCriterioRubrica;

// Saída (GET) de rubricas (níveis) dos critérios
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzCriterioRubricaResponseDTO {

    private Long id;
    private Long criterioId;
    private Integer nivel;
    private String descricaoRubrica;
    private BigDecimal pontuacao;

    public static SapzCriterioRubricaResponseDTO fromEntity(SapzCriterioRubrica obj) {
        return new SapzCriterioRubricaResponseDTO(
                obj.getId(),
                obj.getCriterio().getId(),
                obj.getNivel(),
                obj.getDescricaoRubrica(),
                obj.getPontuacao());
    }
}
