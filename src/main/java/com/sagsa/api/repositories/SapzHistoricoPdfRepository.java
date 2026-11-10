package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.SapzHistoricoPdf;

@Repository
public interface SapzHistoricoPdfRepository extends JpaRepository<SapzHistoricoPdf, Long> {

    List<SapzHistoricoPdf> findBySituacaoAprendizagem_IdOrderByDataGeracaoDesc(Long situacaoAprendizagemId);

    long countBySituacaoAprendizagem_Id(Long situacaoAprendizagemId);

    boolean existsBySituacaoAprendizagem_IdAndVersao(Long situacaoAprendizagemId, String versao);
}
