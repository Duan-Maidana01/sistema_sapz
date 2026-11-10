package com.sagsa.api.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

// Tabela "sapz_historico_pdf": versões de PDF geradas para uma SA (registro imutável)
@Entity
@Table(name = SapzHistoricoPdf.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SapzHistoricoPdf {

    public static final String TABLE_NAME = "sapz_historico_pdf";

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

    @Column(name = "versao", length = 20, nullable = false)
    @NotBlank
    @Size(max = 20)
    private String versao;

    @Column(name = "caminho_arquivo_pdf", length = 255, nullable = false)
    @NotBlank
    @Size(max = 255)
    private String caminhoArquivoPdf;

    // SHA-256 (64 caracteres hexadecimais) do conteúdo do PDF
    @Column(name = "hash_conteudo", length = 64)
    @Size(max = 64)
    private String hashConteudo;

    @CreationTimestamp
    @Column(name = "data_geracao", nullable = false, updatable = false)
    private LocalDateTime dataGeracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por_usuario_id", nullable = false, updatable = false)
    @NotNull
    @ToString.Exclude
    private Usuario criadoPor;
}
