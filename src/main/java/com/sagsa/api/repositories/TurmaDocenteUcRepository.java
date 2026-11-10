package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.TurmaDocenteUc;
import com.sagsa.api.models.TurmaDocenteUcId;

@Repository
public interface TurmaDocenteUcRepository extends JpaRepository<TurmaDocenteUc, TurmaDocenteUcId> {

    List<TurmaDocenteUc> findByTurma_Id(Long turmaId);

    List<TurmaDocenteUc> findByInstrutor_Id(Long instrutorId);

    // Usado para validar se o instrutor leciona a UC na turma
    boolean existsByTurma_IdAndUnidadeCurricular_IdAndInstrutor_Id(Long turmaId, Long unidadeCurricularId, Long instrutorId);
}
