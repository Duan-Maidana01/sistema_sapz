package com.sagsa.api.models.dtos;

import com.sagsa.api.models.Perfil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PerfilResponseDTO {

    private Long id;
    private String nome;
    private String descricao;

    public static PerfilResponseDTO fromEntity(Perfil obj) {
        return new PerfilResponseDTO(obj.getId(), obj.getNome(), obj.getDescricao());
    }
}
