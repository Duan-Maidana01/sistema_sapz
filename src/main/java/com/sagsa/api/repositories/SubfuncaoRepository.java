package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.Subfuncao;

@Repository
public interface SubfuncaoRepository extends JpaRepository<Subfuncao, Long> {

    // Lista os registros de um(a) função
    List<Subfuncao> findByFuncao_IdOrderByCodigoSubfuncaoAsc(Long funcaoId);
}
