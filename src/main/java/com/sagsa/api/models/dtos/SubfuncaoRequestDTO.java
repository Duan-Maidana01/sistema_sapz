package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de subfunções
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubfuncaoRequestDTO {

    @NotNull
    private Long funcaoId;

    @NotBlank
    @Size(max = 20)
    private String codigoSubfuncao;

    @NotBlank
    private String descricao;
}
