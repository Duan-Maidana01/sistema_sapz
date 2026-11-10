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

// Tabela "unidades_curriculares": unidades curriculares (UCs) de cada módulo
@Entity
@Table(name = UnidadeCurricular.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UnidadeCurricular {

    public static final String TABLE_NAME = "unidades_curriculares";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Modulo modulo;

    @Column(name = "nome", length = 150, nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;

    @Column(name = "carga_horaria", nullable = false)
    @NotNull
    @Positive
    private Integer cargaHoraria;

    @Column(name = "objetivo_geral", columnDefinition = "TEXT")
    private String objetivoGeral;

    @Column(name = "ordem", nullable = false)
    @NotNull
    @Min(1)
    private Integer ordem;
}
