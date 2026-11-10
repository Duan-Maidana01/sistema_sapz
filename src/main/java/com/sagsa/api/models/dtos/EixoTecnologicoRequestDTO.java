package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de eixos tecnológicos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EixoTecnologicoRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String nome;
}
