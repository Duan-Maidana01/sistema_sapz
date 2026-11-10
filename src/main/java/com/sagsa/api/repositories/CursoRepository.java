package com.sagsa.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Lista os registros de um(a) área tecnológica
    List<Curso> findByAreaTecnologica_IdOrderByNomeAsc(Long areaTecnologicaId);
}
