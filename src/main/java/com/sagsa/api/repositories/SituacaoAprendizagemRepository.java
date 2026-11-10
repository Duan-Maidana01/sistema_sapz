package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.SituacaoAprendizagem;
import com.sagsa.api.models.projections.SituacaoAprendizagemProjection;

@Repository
public interface SituacaoAprendizagemRepository extends JpaRepository<SituacaoAprendizagem, Long> {

    // Regra de visibilidade de uma SA para um usuário (além de ADMIN/COORDENADOR, que veem todas):
    //  - é o autor; ou
    //  - existe compartilhamento GLOBAL; ou USUARIO direcionado a ele; ou
    //  - ESCOLA / CURSO de alguma turma em que ele leciona ou coordena.
    String VISIBILIDADE = """
            (sa.instrutor.id = :usuarioId
             OR EXISTS (SELECT c.id FROM SapzCompartilhamento c
                        WHERE c.situacaoAprendizagem.id = sa.id
                          AND (c.tipoCompartilhamento = com.sagsa.api.models.enums.TipoCompartilhamentoEnum.GLOBAL
                               OR c.usuarioDestinatario.id = :usuarioId
                               OR c.escola.id IN (SELECT t.escola.id FROM Turma t
                                                  WHERE t.coordenador.id = :usuarioId
                                                     OR t.id IN (SELECT d.turma.id FROM TurmaDocenteUc d WHERE d.instrutor.id = :usuarioId))
                               OR c.curso.id IN (SELECT t2.planoCurso.curso.id FROM Turma t2
                                                 WHERE t2.coordenador.id = :usuarioId
                                                    OR t2.id IN (SELECT d2.turma.id FROM TurmaDocenteUc d2 WHERE d2.instrutor.id = :usuarioId)))))
            """;

    @Query("SELECT sa FROM SituacaoAprendizagem sa WHERE " + VISIBILIDADE + " ORDER BY sa.dataAtualizacao DESC")
    List<SituacaoAprendizagem> findVisiveis(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(sa) > 0 FROM SituacaoAprendizagem sa WHERE sa.id = :saId AND " + VISIBILIDADE)
    boolean isVisivel(@Param("saId") Long saId, @Param("usuarioId") Long usuarioId);

    // SAs de outros autores que estão visíveis ao usuário por compartilhamento
    @Query("SELECT sa FROM SituacaoAprendizagem sa WHERE sa.instrutor.id <> :usuarioId AND " + VISIBILIDADE
            + " ORDER BY sa.dataAtualizacao DESC")
    List<SituacaoAprendizagem> findCompartilhadasComUsuario(@Param("usuarioId") Long usuarioId);

    List<SituacaoAprendizagemProjection> findByInstrutor_IdOrderByDataAtualizacaoDesc(Long instrutorId);

    List<SituacaoAprendizagem> findByUnidadeCurricular_IdOrderByDataAtualizacaoDesc(Long unidadeCurricularId);
}
