package com.sagsa.api.models.dtos;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.DominioCapacidadeEnum;
import com.sagsa.api.models.enums.TipoCapacidadeEnum;

// Entrada (POST/PUT) de capacidades
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapacidadeRequestDTO {

    @NotNull
    private Long unidadeCurricularId;

    @NotNull
    private TipoCapacidadeEnum tipo;

    @NotNull
    private DominioCapacidadeEnum dominio;

    @NotBlank
    private String descricao;

    // Opcional: padrões de desempenho (do mesmo plano de curso) atendidos pela capacidade
    private Set<Long> padraoDesempenhoIds = new HashSet<>();
}
