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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "planos_curso": planos de curso (versões aprovadas de cada curso)
@Entity
@Table(name = PlanoCurso.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(name = "uq_plano_versao", columnNames = {"curso_id", "codigo_plano", "versao"}) })
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlanoCurso {

    public static final String TABLE_NAME = "planos_curso";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Curso curso;

    @Column(name = "codigo_plano", length = 50, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String codigoPlano;

    @Column(name = "versao", length = 20, nullable = false)
    @NotBlank
    @Size(max = 20)
    private String versao;

    @Column(name = "carga_horaria_total", nullable = false)
    @NotNull
    @Positive
    private Integer cargaHorariaTotal;

    @Column(name = "data_aprovacao")
    private LocalDate dataAprovacao;

    @Column(name = "ativo", nullable = false)
    @NotNull
    private Boolean ativo;
}
