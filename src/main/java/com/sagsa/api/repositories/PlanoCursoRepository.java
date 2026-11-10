package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.PlanoCurso;

@Repository
public interface PlanoCursoRepository extends JpaRepository<PlanoCurso, Long> {

    // Lista os registros de um(a) curso
    List<PlanoCurso> findByCurso_IdOrderByCodigoPlanoAsc(Long cursoId);
}
