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

// Tabela "subfuncoes": subfunções de cada função
@Entity
@Table(name = Subfuncao.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(name = "uq_subfuncao_funcao", columnNames = {"funcao_id", "codigo_subfuncao"}) })
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Subfuncao {

    public static final String TABLE_NAME = "subfuncoes";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Funcao funcao;

    @Column(name = "codigo_subfuncao", length = 20, nullable = false)
    @NotBlank
    @Size(max = 20)
    private String codigoSubfuncao;

    @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String descricao;
}
