package com.sagsa.api.models.dtos;

import java.time.LocalDateTime;

import com.sagsa.api.models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Saída de usuário (nunca expõe o hash da senha)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Long perfilId;
    private String perfil;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public static UsuarioResponseDTO fromEntity(Usuario obj) {
        return new UsuarioResponseDTO(
                obj.getId(),
                obj.getNome(),
                obj.getEmail(),
                obj.getPerfil().getId(),
                obj.getPerfil().getNome(),
                obj.getAtivo(),
                obj.getDataCriacao(),
                obj.getDataAtualizacao());
    }
}
