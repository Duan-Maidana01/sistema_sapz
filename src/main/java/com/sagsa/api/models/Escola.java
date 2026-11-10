package com.sagsa.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Tabela "escolas": unidades escolares
@Entity
@Table(name = Escola.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Escola {

    public static final String TABLE_NAME = "escolas";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", length = 150, nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;

    @Column(name = "codigo_unidade", length = 20, unique = true)
    @Size(max = 20)
    private String codigoUnidade;

    @Column(name = "cidade", length = 100, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String cidade;

    @Column(name = "estado", columnDefinition = "CHAR(2)", length = 2, nullable = false)
    @NotBlank
    @Size(min = 2, max = 2)
    @Pattern(regexp = "[A-Za-z]{2}", message = "deve ser a sigla da UF com 2 letras")
    private String estado;

    @Column(name = "ativo", nullable = false)
    @NotNull
    private Boolean ativo;
}
