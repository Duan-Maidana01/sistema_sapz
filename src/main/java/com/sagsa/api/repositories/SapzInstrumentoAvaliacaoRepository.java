package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.SapzInstrumentoAvaliacao;

@Repository
public interface SapzInstrumentoAvaliacaoRepository extends JpaRepository<SapzInstrumentoAvaliacao, Long> {

    // Lista os registros de um(a) situação de aprendizagem
    List<SapzInstrumentoAvaliacao> findBySituacaoAprendizagem_IdOrderByIdAsc(Long situacaoAprendizagemId);
}
