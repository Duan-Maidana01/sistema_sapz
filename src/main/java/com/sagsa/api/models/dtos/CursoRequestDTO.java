package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.TipoCursoEnum;

// Entrada (POST/PUT) de cursos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CursoRequestDTO {

    // Opcional
    private Long areaTecnologicaId;

    @NotBlank
    @Size(max = 150)
    private String nome;

    @NotNull
    private TipoCursoEnum tipoCurso;

    // Opcional (padrão: true)
    private Boolean ativo;
}
