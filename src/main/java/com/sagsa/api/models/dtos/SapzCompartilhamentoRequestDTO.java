package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.enums.TipoCompartilhamentoEnum;

// Entrada (POST) de compartilhamento.
// GLOBAL: nenhum alvo | ESCOLA: escolaId | CURSO: cursoId | USUARIO: usuarioDestinatarioId
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzCompartilhamentoRequestDTO {

    @NotNull
    private Long situacaoAprendizagemId;

    @NotNull
    private TipoCompartilhamentoEnum tipoCompartilhamento;

    private Long escolaId;

    private Long cursoId;

    private Long usuarioDestinatarioId;
}
