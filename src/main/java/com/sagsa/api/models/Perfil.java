package com.sagsa.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Tabela "perfis": perfis de acesso (ADMIN, COORDENADOR, INSTRUTOR)
@Entity
@Table(name = Perfil.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Perfil {

    public static final String TABLE_NAME = "perfis";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", length = 50, nullable = false, unique = true)
    @NotBlank
    @Size(max = 50)
    private String nome;

    @Column(name = "descricao", length = 255)
    @Size(max = 255)
    private String descricao;
}
