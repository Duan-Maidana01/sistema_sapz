package com.sagsa.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.sagsa.api.models.enums.PerfilEnum;

// Testes unitários simples (não sobem o contexto, pois exigem o banco sistema_sapz)
class ApiApplicationTests {

    @Test
    void perfilEnumConverteNomeDoBancoParaRole() {
        assertEquals(PerfilEnum.ADMIN, PerfilEnum.fromNome("admin"));
        assertEquals("ROLE_INSTRUTOR", PerfilEnum.fromNome("INSTRUTOR").getRole());
        assertThrows(IllegalArgumentException.class, () -> PerfilEnum.fromNome("DESCONHECIDO"));
    }

}
