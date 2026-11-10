package com.sagsa.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Tabela "alunos": alunos
@Entity
@Table(name = Aluno.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aluno {

    public static final String TABLE_NAME = "alunos";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String nome;

    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    @NotBlank
    @Size(max = 14)
    @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}", message = "CPF inválido (use 000.000.000-00)")
    private String cpf;

    @Column(name = "email", length = 150)
    @Size(max = 150)
    @Email
    private String email;

    @Column(name = "matricula", length = 50, nullable = false, unique = true)
    @NotBlank
    @Size(max = 50)
    private String matricula;
}
