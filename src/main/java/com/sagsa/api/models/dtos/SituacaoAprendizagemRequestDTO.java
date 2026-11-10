package com.sagsa.api.models.dtos;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.TipoEstrategiaEnum;

// Entrada (POST/PUT) de Situação de Aprendizagem
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SituacaoAprendizagemRequestDTO {

    @NotBlank
    @Size(max = 200)
    private String titulo;

    @NotNull
    private Long unidadeCurricularId;

    // Opcional
    private Long turmaId;

    @NotNull
    private TipoEstrategiaEnum tipoEstrategia;

    @NotBlank
    private String contextualizacao;

    @NotBlank
    private String desafio;

    @NotBlank
    private String resultadosEsperados;

    @NotNull
    @Positive
    private Integer cargaHorariaSa;

    // Capacidades da UC trabalhadas na SA (ao menos uma)
    @NotEmpty
    private Set<Long> capacidadeIds = new HashSet<>();

    // Conhecimentos da UC mobilizados na SA (opcional)
    private Set<Long> conhecimentoIds = new HashSet<>();
}
