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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.sagsa.api.models.enums.TipoCursoEnum;

// Tabela "cursos": cursos ofertados
@Entity
@Table(name = Curso.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Curso {

    public static final String TABLE_NAME = "cursos";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_tecnologica_id")
    @ToString.Exclude
    private AreaTecnologica areaTecnologica;

    @Column(name = "nome", length = 150, nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_curso", length = 50, nullable = false)
    @NotNull
    private TipoCursoEnum tipoCurso;

    @Column(name = "ativo", nullable = false)
    @NotNull
    private Boolean ativo;
}
