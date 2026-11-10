package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    boolean existsByCodigoTurma(String codigoTurma);

    boolean existsByCodigoTurmaAndIdNot(String codigoTurma, Long id);

    List<Turma> findByEscola_IdOrderByCodigoTurmaAsc(Long escolaId);

    List<Turma> findByPlanoCurso_IdOrderByCodigoTurmaAsc(Long planoCursoId);

    // Turmas em que o usuário é coordenador ou leciona alguma UC
    @Query("""
            SELECT t FROM Turma t
            WHERE t.coordenador.id = :usuarioId
               OR t.id IN (SELECT d.turma.id FROM TurmaDocenteUc d WHERE d.instrutor.id = :usuarioId)
            ORDER BY t.ano DESC, t.semestre DESC, t.codigoTurma ASC
            """)
    List<Turma> findVinculadasAoUsuario(@Param("usuarioId") Long usuarioId);
}
