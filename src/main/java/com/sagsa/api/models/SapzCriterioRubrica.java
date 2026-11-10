package com.sagsa.api.models;

import java.math.BigDecimal;

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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "sapz_criterio_rubricas": rubricas/níveis de desempenho de cada critério
@Entity
@Table(name = SapzCriterioRubrica.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(name = "uq_criterio_nivel", columnNames = {"criterio_id", "nivel"}) })
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SapzCriterioRubrica {

    public static final String TABLE_NAME = "sapz_criterio_rubricas";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criterio_id", nullable = false, updatable = false)
    @NotNull
    @ToString.Exclude
    private SapzCriterioAvaliacao criterio;

    @Column(name = "nivel", nullable = false)
    @NotNull
    @Min(1)
    private Integer nivel;

    @Column(name = "descricao_rubrica", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String descricaoRubrica;

    @Column(name = "pontuacao", precision = 5, scale = 2, nullable = false)
    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal pontuacao;
}
