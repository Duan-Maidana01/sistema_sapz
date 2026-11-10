package com.sagsa.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
