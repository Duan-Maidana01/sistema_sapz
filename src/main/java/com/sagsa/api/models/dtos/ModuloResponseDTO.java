package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Modulo;
import com.sagsa.api.models.enums.TipoModuloEnum;

// Saída (GET) de módulos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModuloResponseDTO {

    private Long id;
    private Long planoCursoId;
    private String planoCursoCodigoPlano;
    private String nome;
    private TipoModuloEnum tipoModulo;
    private Integer ordem;
    private Integer cargaHoraria;

    public static ModuloResponseDTO fromEntity(Modulo obj) {
        return new ModuloResponseDTO(
                obj.getId(),
                obj.getPlanoCurso().getId(),
                obj.getPlanoCurso().getCodigoPlano(),
                obj.getNome(),
                obj.getTipoModulo(),
                obj.getOrdem(),
                obj.getCargaHoraria());
    }
}
