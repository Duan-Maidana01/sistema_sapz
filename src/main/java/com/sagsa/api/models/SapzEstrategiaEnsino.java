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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "sapz_estrategias_ensino": estratégias de ensino de uma SA
@Entity
@Table(name = SapzEstrategiaEnsino.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SapzEstrategiaEnsino {

    public static final String TABLE_NAME = "sapz_estrategias_ensino";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "situacao_aprendizagem_id", nullable = false, updatable = false)
    @NotNull
    @ToString.Exclude
    private SituacaoAprendizagem situacaoAprendizagem;

    @Column(name = "nome_estrategia", length = 100, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String nomeEstrategia;

    @Column(name = "descricao_atividade", columnDefinition = "TEXT")
    private String descricaoAtividade;
}
