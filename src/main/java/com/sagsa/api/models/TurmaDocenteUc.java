package com.sagsa.api.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Tabela "turma_docentes_ucs": qual instrutor ministra qual UC em cada turma
@Entity
@Table(name = TurmaDocenteUc.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TurmaDocenteUc {

    public static final String TABLE_NAME = "turma_docentes_ucs";

    @EmbeddedId
    @EqualsAndHashCode.Include
    private TurmaDocenteUcId id = new TurmaDocenteUcId();

    @MapsId("turmaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    @ToString.Exclude
    private Turma turma;

    @MapsId("unidadeCurricularId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_curricular_id")
    @ToString.Exclude
    private UnidadeCurricular unidadeCurricular;

    @MapsId("instrutorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    @ToString.Exclude
    private Usuario instrutor;

    public TurmaDocenteUc(Turma turma, UnidadeCurricular unidadeCurricular, Usuario instrutor) {
        this.id = new TurmaDocenteUcId(turma.getId(), unidadeCurricular.getId(), instrutor.getId());
        this.turma = turma;
        this.unidadeCurricular = unidadeCurricular;
        this.instrutor = instrutor;
    }
}
