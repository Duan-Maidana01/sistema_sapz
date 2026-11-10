package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Funcao;

// Saída (GET) de funções do perfil profissional
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FuncaoResponseDTO {

    private Long id;
    private Long planoCursoId;
    private String planoCursoCodigoPlano;
    private Integer numero;
    private String descricao;

    public static FuncaoResponseDTO fromEntity(Funcao obj) {
        return new FuncaoResponseDTO(
                obj.getId(),
                obj.getPlanoCurso().getId(),
                obj.getPlanoCurso().getCodigoPlano(),
                obj.getNumero(),
                obj.getDescricao());
    }
}
