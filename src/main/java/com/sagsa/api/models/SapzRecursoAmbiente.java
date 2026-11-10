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

// Tabela "sapz_recursos_ambientes": ambientes pedagógicos e recursos didáticos de uma SA
@Entity
@Table(name = SapzRecursoAmbiente.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SapzRecursoAmbiente {

    public static final String TABLE_NAME = "sapz_recursos_ambientes";

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

    @Column(name = "ambiente_pedagogico", length = 150, nullable = false)
    @NotBlank
    @Size(max = 150)
    private String ambientePedagogico;

    @Column(name = "recursos_didaticos", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String recursosDidaticos;
}
