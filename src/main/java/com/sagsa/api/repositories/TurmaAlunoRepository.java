package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.TurmaAluno;
import com.sagsa.api.models.TurmaAlunoId;

@Repository
public interface TurmaAlunoRepository extends JpaRepository<TurmaAluno, TurmaAlunoId> {

    List<TurmaAluno> findByTurma_IdOrderByAluno_NomeAsc(Long turmaId);

    List<TurmaAluno> findByAluno_IdOrderByDataMatriculaDesc(Long alunoId);
}
