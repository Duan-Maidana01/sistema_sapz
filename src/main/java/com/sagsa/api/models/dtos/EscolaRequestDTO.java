package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de escolas
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EscolaRequestDTO {

    @NotBlank
    @Size(max = 150)
    private String nome;

    @Size(max = 20)
    private String codigoUnidade;

    @NotBlank
    @Size(max = 100)
    private String cidade;

    // Opcional (padrão: RS)
    @Size(min = 2, max = 2)
    @Pattern(regexp = "[A-Za-z]{2}", message = "deve ser a sigla da UF com 2 letras")
    private String estado;

    // Opcional (padrão: true)
    private Boolean ativo;
}
