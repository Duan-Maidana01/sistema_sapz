package com.sagsa.api.models.enums;

// Situação do aluno na turma (turma_alunos.status)
// Persistido como texto (EnumType.STRING): os valores gravados no banco devem ser exatamente estes.
public enum StatusMatriculaEnum {

    MATRICULADO,
    CURSANDO,
    CONCLUIDO,
    TRANCADO,
    DESISTENTE,
    TRANSFERIDO,
    REPROVADO
}
