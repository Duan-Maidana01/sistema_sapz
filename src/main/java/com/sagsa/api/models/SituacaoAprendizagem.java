package com.sagsa.api.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.sagsa.api.models.enums.TipoEstrategiaEnum;

// Tabela "situacoes_aprendizagem": Situação de Aprendizagem (SA / SAPZ) elaborada pelo instrutor
@Entity
@Table(name = SituacaoAprendizagem.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SituacaoAprendizagem {

    public static final String TABLE_NAME = "situacoes_aprendizagem";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "titulo", length = 200, nullable = false)
    @NotBlank
    @Size(max = 200)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_curricular_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private UnidadeCurricular unidadeCurricular;

    // Autor da SA (definido pelo usuário logado; não pode ser alterado)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id", nullable = false, updatable = false)
    @NotNull
    @ToString.Exclude
    private Usuario instrutor;

    // Turma (opcional) em que a SA será aplicada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    @ToString.Exclude
    private Turma turma;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estrategia", length = 50, nullable = false)
    @NotNull
    private TipoEstrategiaEnum tipoEstrategia;

    @Column(name = "contextualizacao", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String contextualizacao;

    @Column(name = "desafio", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String desafio;

    @Column(name = "resultados_esperados", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String resultadosEsperados;

    @Column(name = "carga_horaria_sa", nullable = false)
    @NotNull
    @Positive
    private Integer cargaHorariaSa;

    // Controlado pelo serviço de compartilhamentos
    @Column(name = "compartilhada", nullable = false)
    @NotNull
    private Boolean compartilhada;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    // Tabela associativa "sapz_capacidades" (capacidades trabalhadas na SA)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sapz_capacidades",
        joinColumns = @JoinColumn(name = "situacao_aprendizagem_id"),
        inverseJoinColumns = @JoinColumn(name = "capacidade_id")
    )
    @ToString.Exclude
    private Set<Capacidade> capacidades = new HashSet<>();

    // Tabela associativa "sapz_conhecimentos" (conhecimentos mobilizados na SA)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sapz_conhecimentos",
        joinColumns = @JoinColumn(name = "situacao_aprendizagem_id"),
        inverseJoinColumns = @JoinColumn(name = "conhecimento_id")
    )
    @ToString.Exclude
    private Set<Conhecimento> conhecimentos = new HashSet<>();
}
