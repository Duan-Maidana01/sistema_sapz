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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "sapz_criterios_avaliacao": critérios de avaliação de um instrumento, vinculados a uma capacidade
@Entity
@Table(name = SapzCriterioAvaliacao.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SapzCriterioAvaliacao {

    public static final String TABLE_NAME = "sapz_criterios_avaliacao";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrumento_id", nullable = false, updatable = false)
    @NotNull
    @ToString.Exclude
    private SapzInstrumentoAvaliacao instrumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capacidade_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Capacidade capacidade;

    @Column(name = "descricao_criterio", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String descricaoCriterio;

    @Column(name = "prioridade_peso", precision = 5, scale = 2)
    @DecimalMin(value = "0.01")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal prioridadePeso;
}
