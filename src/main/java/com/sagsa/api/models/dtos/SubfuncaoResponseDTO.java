package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Subfuncao;

// Saída (GET) de subfunções
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubfuncaoResponseDTO {

    private Long id;
    private Long funcaoId;
    private Integer funcaoNumero;
    private String codigoSubfuncao;
    private String descricao;

    public static SubfuncaoResponseDTO fromEntity(Subfuncao obj) {
        return new SubfuncaoResponseDTO(
                obj.getId(),
                obj.getFuncao().getId(),
                obj.getFuncao().getNumero(),
                obj.getCodigoSubfuncao(),
                obj.getDescricao());
    }
}
