package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.TipoMetodoAvaliacaoEnum;

// Entrada (POST/PUT) de instrumentos de avaliação da SA
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzInstrumentoAvaliacaoRequestDTO {

    @NotNull
    private Long situacaoAprendizagemId;

    @NotBlank
    @Size(max = 100)
    private String nomeInstrumento;

    private String descricao;

    // Opcional (padrão: GRADUAL)
    private TipoMetodoAvaliacaoEnum tipoMetodo;
}
