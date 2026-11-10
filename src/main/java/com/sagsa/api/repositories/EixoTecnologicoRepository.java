package com.sagsa.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.EixoTecnologico;

@Repository
public interface EixoTecnologicoRepository extends JpaRepository<EixoTecnologico, Long> {
}
