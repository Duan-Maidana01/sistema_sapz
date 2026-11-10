package com.sagsa.api.models;

import java.util.HashSet;
import java.util.Set;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.sagsa.api.models.enums.DominioCapacidadeEnum;
import com.sagsa.api.models.enums.TipoCapacidadeEnum;

// Tabela "capacidades": capacidades (básicas, técnicas, socioemocionais) de cada UC
@Entity
@Table(name = Capacidade.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Capacidade {

    public static final String TABLE_NAME = "capacidades";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_curricular_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private UnidadeCurricular unidadeCurricular;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 30, nullable = false)
    @NotNull
    private TipoCapacidadeEnum tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "dominio", length = 30, nullable = false)
    @NotNull
    private DominioCapacidadeEnum dominio;

    @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String descricao;

    // Tabela associativa "capacidade_padrao_desempenho" (N:N com padroes_desempenho)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "capacidade_padrao_desempenho",
        joinColumns = @JoinColumn(name = "capacidade_id"),
        inverseJoinColumns = @JoinColumn(name = "padrao_desempenho_id")
    )
    @ToString.Exclude
    private Set<PadraoDesempenho> padroesDesempenho = new HashSet<>();
}
