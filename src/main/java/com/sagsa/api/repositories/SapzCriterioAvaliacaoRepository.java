package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.SapzCriterioAvaliacao;

@Repository
public interface SapzCriterioAvaliacaoRepository extends JpaRepository<SapzCriterioAvaliacao, Long> {

    // Lista os registros de um(a) instrumento de avaliação
    List<SapzCriterioAvaliacao> findByInstrumento_IdOrderByIdAsc(Long instrumentoId);

    // Verifica se alguma capacidade da SA já é usada em critérios (impede removê-la da SA)
    boolean existsByInstrumento_SituacaoAprendizagem_IdAndCapacidade_Id(Long situacaoAprendizagemId, Long capacidadeId);
}
