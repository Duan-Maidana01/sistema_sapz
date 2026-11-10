package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.PadraoDesempenho;

// Saída (GET) de padrões de desempenho
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PadraoDesempenhoResponseDTO {

    private Long id;
    private Long subfuncaoId;
    private String subfuncaoCodigoSubfuncao;
    private String descricao;

    public static PadraoDesempenhoResponseDTO fromEntity(PadraoDesempenho obj) {
        return new PadraoDesempenhoResponseDTO(
                obj.getId(),
                obj.getSubfuncao().getId(),
                obj.getSubfuncao().getCodigoSubfuncao(),
                obj.getDescricao());
    }
}
