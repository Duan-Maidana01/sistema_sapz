package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.EixoTecnologico;

// Saída (GET) de eixos tecnológicos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EixoTecnologicoResponseDTO {

    private Long id;
    private String nome;

    public static EixoTecnologicoResponseDTO fromEntity(EixoTecnologico obj) {
        return new EixoTecnologicoResponseDTO(
                obj.getId(),
                obj.getNome());
    }
}
