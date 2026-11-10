package com.sagsa.api.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "usuarios": usuários que acessam o sistema (login por e-mail)
@Entity
@Table(name = Usuario.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    public static final String TABLE_NAME = "usuarios";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    @NotBlank
    @Size(min = 2, max = 100)
    private String nome;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    // Hash BCrypt da senha: nunca é devolvido no JSON
    @JsonProperty(access = Access.WRITE_ONLY)
    @ToString.Exclude
    @Column(name = "senha_hash", length = 255, nullable = false)
    @NotBlank
    @Size(max = 255)
    private String senhaHash;

    // Perfil carregado junto (EAGER) pois é usado em toda requisição autenticada
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id", nullable = false)
    @NotNull
    private Perfil perfil;

    @Column(name = "ativo", nullable = false)
    @NotNull
    private Boolean ativo;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;
}
