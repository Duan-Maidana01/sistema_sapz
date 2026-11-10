package com.sagsa.api.models.dtos;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Curso;
import com.sagsa.api.models.enums.TipoCursoEnum;

// Saída (GET) de cursos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CursoResponseDTO {

    private Long id;
    private Long areaTecnologicaId;
    private String areaTecnologicaNome;
    private String nome;
    private TipoCursoEnum tipoCurso;
    private Boolean ativo;

    public static CursoResponseDTO fromEntity(Curso obj) {
        return new CursoResponseDTO(
                obj.getId(),
                Objects.nonNull(obj.getAreaTecnologica()) ? obj.getAreaTecnologica().getId() : null,
                Objects.nonNull(obj.getAreaTecnologica()) ? obj.getAreaTecnologica().getNome() : null,
                obj.getNome(),
                obj.getTipoCurso(),
                obj.getAtivo());
    }
}
