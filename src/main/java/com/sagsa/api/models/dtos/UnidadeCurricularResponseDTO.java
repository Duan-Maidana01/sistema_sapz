package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.UnidadeCurricular;

// Saída (GET) de unidades curriculares
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnidadeCurricularResponseDTO {

    private Long id;
    private Long moduloId;
    private String moduloNome;
    private String nome;
    private Integer cargaHoraria;
    private String objetivoGeral;
    private Integer ordem;

    public static UnidadeCurricularResponseDTO fromEntity(UnidadeCurricular obj) {
        return new UnidadeCurricularResponseDTO(
                obj.getId(),
                obj.getModulo().getId(),
                obj.getModulo().getNome(),
                obj.getNome(),
                obj.getCargaHoraria(),
                obj.getObjetivoGeral(),
                obj.getOrdem());
    }
}
