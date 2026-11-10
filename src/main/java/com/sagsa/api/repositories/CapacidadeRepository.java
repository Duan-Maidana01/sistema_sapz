package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.Capacidade;
import com.sagsa.api.models.enums.TipoCapacidadeEnum;

@Repository
public interface CapacidadeRepository extends JpaRepository<Capacidade, Long> {

    // Lista as capacidades de uma unidade curricular
    List<Capacidade> findByUnidadeCurricular_IdOrderByIdAsc(Long unidadeCurricularId);

    // Lista as capacidades de uma UC filtrando pelo tipo (BASICA, TECNICA, SOCIOEMOCIONAL)
    List<Capacidade> findByUnidadeCurricular_IdAndTipoOrderByIdAsc(Long unidadeCurricularId, TipoCapacidadeEnum tipo);
}
