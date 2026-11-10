package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.AreaTecnologica;

// Saída (GET) de áreas tecnológicas
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AreaTecnologicaResponseDTO {

    private Long id;
    private Long eixoTecnologicoId;
    private String eixoTecnologicoNome;
    private String nome;

    public static AreaTecnologicaResponseDTO fromEntity(AreaTecnologica obj) {
        return new AreaTecnologicaResponseDTO(
                obj.getId(),
                obj.getEixoTecnologico().getId(),
                obj.getEixoTecnologico().getNome(),
                obj.getNome());
    }
}
