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

import com.sagsa.api.models.enums.TipoMetodoAvaliacaoEnum;

// Tabela "sapz_instrumentos_avaliacao": instrumentos de avaliação de uma SA
@Entity
@Table(name = SapzInstrumentoAvaliacao.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SapzInstrumentoAvaliacao {

    public static final String TABLE_NAME = "sapz_instrumentos_avaliacao";

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

    @Column(name = "nome_instrumento", length = 100, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String nomeInstrumento;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_metodo", length = 30, nullable = false)
    @NotNull
    private TipoMetodoAvaliacaoEnum tipoMetodo;
}
