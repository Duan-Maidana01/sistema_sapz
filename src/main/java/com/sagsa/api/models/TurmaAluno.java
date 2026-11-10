package com.sagsa.api.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.sagsa.api.models.enums.StatusMatriculaEnum;

// Tabela "turma_alunos": matrícula de um aluno em uma turma
@Entity
@Table(name = TurmaAluno.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TurmaAluno {

    public static final String TABLE_NAME = "turma_alunos";

    @EmbeddedId
    @EqualsAndHashCode.Include
    private TurmaAlunoId id = new TurmaAlunoId();

    @MapsId("turmaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    @ToString.Exclude
    private Turma turma;

    @MapsId("alunoId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    @ToString.Exclude
    private Aluno aluno;

    @Column(name = "data_matricula", nullable = false)
    @NotNull
    private LocalDate dataMatricula;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    @NotNull
    private StatusMatriculaEnum status;
}
