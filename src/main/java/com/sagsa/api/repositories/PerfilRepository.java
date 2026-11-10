package com.sagsa.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sagsa.api.models.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Transactional(readOnly = true)
    Optional<Perfil> findByNome(String nome);

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);
}
