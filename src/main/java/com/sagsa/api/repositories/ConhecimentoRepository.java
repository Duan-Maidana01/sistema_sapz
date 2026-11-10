package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.Conhecimento;

@Repository
public interface ConhecimentoRepository extends JpaRepository<Conhecimento, Long> {

    // Lista os registros de um(a) unidade curricular
    List<Conhecimento> findByUnidadeCurricular_IdOrderByIdAsc(Long unidadeCurricularId);
}
