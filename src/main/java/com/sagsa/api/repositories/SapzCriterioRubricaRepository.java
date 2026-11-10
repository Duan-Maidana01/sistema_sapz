package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.SapzCriterioRubrica;

@Repository
public interface SapzCriterioRubricaRepository extends JpaRepository<SapzCriterioRubrica, Long> {

    // Lista os registros de um(a) critério de avaliação
    List<SapzCriterioRubrica> findByCriterio_IdOrderByNivelAsc(Long criterioId);

    boolean existsByCriterio_IdAndNivel(Long criterioId, Integer nivel);

    boolean existsByCriterio_IdAndNivelAndIdNot(Long criterioId, Integer nivel, Long id);
}
