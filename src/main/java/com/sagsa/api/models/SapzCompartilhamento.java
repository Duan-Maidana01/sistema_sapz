package com.sagsa.api.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.sagsa.api.models.enums.TipoCompartilhamentoEnum;

// Tabela "sapz_compartilhamentos": com quem a SA foi compartilhada (GLOBAL, ESCOLA, CURSO ou USUARIO)
@Entity
@Table(name = SapzCompartilhamento.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SapzCompartilhamento {

    public static final String TABLE_NAME = "sapz_compartilhamentos";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "situacao_aprendizagem_id", nullable = false, updatable = false)
    @NotNull
    @ToString.Exclude
    private SituacaoAprendizagem situacaoAprendizagem;

    // Preenchido somente quando tipo = ESCOLA
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id")
    @ToString.Exclude
    private Escola escola;

    // Preenchido somente quando tipo = CURSO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    @ToString.Exclude
    private Curso curso;

    // Preenchido somente quando tipo = USUARIO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destinatario_id")
    @ToString.Exclude
    private Usuario usuarioDestinatario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_compartilhamento", length = 30, nullable = false)
    @NotNull
    private TipoCompartilhamentoEnum tipoCompartilhamento;

    @CreationTimestamp
    @Column(name = "data_compartilhamento", nullable = false, updatable = false)
    private LocalDateTime dataCompartilhamento;
}
