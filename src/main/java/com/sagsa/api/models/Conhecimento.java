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

// Tabela "conhecimentos": conhecimentos (conteúdos) de cada UC
@Entity
@Table(name = Conhecimento.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Conhecimento {

    public static final String TABLE_NAME = "conhecimentos";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_curricular_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private UnidadeCurricular unidadeCurricular;

    @Column(name = "topico", length = 200, nullable = false)
    @NotBlank
    @Size(max = 200)
    private String topico;

    @Column(name = "detalhamento", columnDefinition = "TEXT")
    private String detalhamento;
}
