package com.sagsa.api.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "turmas": turmas de um plano de curso em uma escola
@Entity
@Table(name = Turma.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Turma {

    public static final String TABLE_NAME = "turmas";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Escola escola;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_curso_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private PlanoCurso planoCurso;

    // Coordenador responsável (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordenador_id")
    @ToString.Exclude
    private Usuario coordenador;

    @Column(name = "codigo_turma", length = 50, nullable = false, unique = true)
    @NotBlank
    @Size(max = 50)
    private String codigoTurma;

    @Column(name = "ano", nullable = false)
    @NotNull
    @Min(2000)
    @Max(2100)
    private Integer ano;

    @Column(name = "semestre", nullable = false)
    @NotNull
    @Min(1)
    @Max(2)
    private Integer semestre;

    @Column(name = "data_inicio", nullable = false)
    @NotNull
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    @NotNull
    private LocalDate dataFim;

    @Column(name = "ativo", nullable = false)
    @NotNull
    private Boolean ativo;
}
