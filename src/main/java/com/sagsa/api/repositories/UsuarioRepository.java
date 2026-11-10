package com.sagsa.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sagsa.api.models.Usuario;
import com.sagsa.api.models.projections.UsuarioProjection;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Usado no login (e-mail é o "username")
    @Transactional(readOnly = true)
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPerfil_Nome(String nome);

    // Lista usuários ativos de um perfil (ex.: INSTRUTOR) em formato resumido
    List<UsuarioProjection> findByPerfil_NomeAndAtivoTrueOrderByNomeAsc(String nome);
}
