package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Conhecimento;

// Saída (GET) de conhecimentos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConhecimentoResponseDTO {

    private Long id;
    private Long unidadeCurricularId;
    private String unidadeCurricularNome;
    private String topico;
    private String detalhamento;

    public static ConhecimentoResponseDTO fromEntity(Conhecimento obj) {
        return new ConhecimentoResponseDTO(
                obj.getId(),
                obj.getUnidadeCurricular().getId(),
                obj.getUnidadeCurricular().getNome(),
                obj.getTopico(),
                obj.getDetalhamento());
    }
}
