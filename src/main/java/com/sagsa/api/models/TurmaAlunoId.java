package com.sagsa.api.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Chave primária composta de "turma_alunos" (turma_id, aluno_id)
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaAlunoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "turma_id")
    private Long turmaId;

    @Column(name = "aluno_id")
    private Long alunoId;
}
