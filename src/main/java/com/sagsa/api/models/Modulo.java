package com.sagsa.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.sagsa.api.models.enums.TipoModuloEnum;

// Tabela "modulos": módulos do desenho curricular
@Entity
@Table(name = Modulo.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Modulo {

    public static final String TABLE_NAME = "modulos";

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

    @Column(name = "nome", length = 100, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_modulo", length = 30, nullable = false)
    @NotNull
    private TipoModuloEnum tipoModulo;

    @Column(name = "ordem", nullable = false)
    @NotNull
    @Min(1)
    private Integer ordem;

    @Column(name = "carga_horaria", nullable = false)
    @NotNull
    @Positive
    private Integer cargaHoraria;
}
