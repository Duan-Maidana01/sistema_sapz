package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Escola;

// Saída (GET) de escolas
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EscolaResponseDTO {

    private Long id;
    private String nome;
    private String codigoUnidade;
    private String cidade;
    private String estado;
    private Boolean ativo;

    public static EscolaResponseDTO fromEntity(Escola obj) {
        return new EscolaResponseDTO(
                obj.getId(),
                obj.getNome(),
                obj.getCodigoUnidade(),
                obj.getCidade(),
                obj.getEstado(),
                obj.getAtivo());
    }
}
