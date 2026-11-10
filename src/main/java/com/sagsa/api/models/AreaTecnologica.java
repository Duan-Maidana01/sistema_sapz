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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "areas_tecnologicas": áreas tecnológicas de cada eixo
@Entity
@Table(name = AreaTecnologica.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(name = "uq_eixo_area", columnNames = {"eixo_tecnologico_id", "nome"}) })
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AreaTecnologica {

    public static final String TABLE_NAME = "areas_tecnologicas";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eixo_tecnologico_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private EixoTecnologico eixoTecnologico;

    @Column(name = "nome", length = 100, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String nome;
}
