package com.sagsa.api.models;

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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "funcoes": funções do perfil profissional do plano de curso
@Entity
@Table(name = Funcao.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(name = "uq_funcao_plano", columnNames = {"plano_curso_id", "numero"}) })
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Funcao {

    public static final String TABLE_NAME = "funcoes";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_curso_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private PlanoCurso planoCurso;

    @Column(name = "numero", nullable = false)
    @NotNull
    @Min(1)
    private Integer numero;

    @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String descricao;
}
