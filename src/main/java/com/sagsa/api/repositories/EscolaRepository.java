package com.sagsa.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagsa.api.models.Escola;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
}
