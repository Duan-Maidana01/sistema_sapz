package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.SapzRecursoAmbiente;

@Repository
public interface SapzRecursoAmbienteRepository extends JpaRepository<SapzRecursoAmbiente, Long> {

    // Lista os registros de um(a) situação de aprendizagem
    List<SapzRecursoAmbiente> findBySituacaoAprendizagem_IdOrderByIdAsc(Long situacaoAprendizagemId);
}
