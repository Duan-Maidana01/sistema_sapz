package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST/PUT) de conhecimentos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConhecimentoRequestDTO {

    @NotNull
    private Long unidadeCurricularId;

    @NotBlank
    @Size(max = 200)
    private String topico;

    private String detalhamento;
}
