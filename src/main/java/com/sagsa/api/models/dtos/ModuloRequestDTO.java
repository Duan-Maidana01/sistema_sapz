package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.TipoModuloEnum;

// Entrada (POST/PUT) de módulos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModuloRequestDTO {

    @NotNull
    private Long planoCursoId;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotNull
    private TipoModuloEnum tipoModulo;

    // Opcional (padrão: 1)
    @Min(1)
    private Integer ordem;

    @NotNull
    @Positive
    private Integer cargaHoraria;
}
