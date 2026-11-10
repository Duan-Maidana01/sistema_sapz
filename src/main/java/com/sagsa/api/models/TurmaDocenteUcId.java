package com.sagsa.api.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Chave primária composta de "turma_docentes_ucs" (turma_id, unidade_curricular_id, instrutor_id)
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaDocenteUcId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "turma_id")
    private Long turmaId;

    @Column(name = "unidade_curricular_id")
    private Long unidadeCurricularId;

    @Column(name = "instrutor_id")
    private Long instrutorId;
}
