package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.AreaTecnologica;

@Repository
public interface AreaTecnologicaRepository extends JpaRepository<AreaTecnologica, Long> {

    // Lista os registros de um(a) eixo tecnológico
    List<AreaTecnologica> findByEixoTecnologico_IdOrderByNomeAsc(Long eixoTecnologicoId);
}
