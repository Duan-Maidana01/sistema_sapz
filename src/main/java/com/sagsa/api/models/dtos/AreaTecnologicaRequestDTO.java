package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de áreas tecnológicas
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AreaTecnologicaRequestDTO {

    @NotNull
    private Long eixoTecnologicoId;

    @NotBlank
    @Size(max = 100)
    private String nome;
}
