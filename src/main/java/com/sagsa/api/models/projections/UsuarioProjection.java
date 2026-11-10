package com.sagsa.api.models.projections;

// Projeção resumida de usuário (ex.: listagem de instrutores para seleção)
public interface UsuarioProjection {

    Long getId();

    String getNome();

    String getEmail();
}
