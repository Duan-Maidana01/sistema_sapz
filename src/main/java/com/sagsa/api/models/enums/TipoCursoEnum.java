package com.sagsa.api.models.enums;

// Tipos de curso da Educação Profissional SENAI (cursos.tipo_curso)
// Persistido como texto (EnumType.STRING): os valores gravados no banco devem ser exatamente estes.
public enum TipoCursoEnum {

    INICIACAO_PROFISSIONAL,
    QUALIFICACAO_PROFISSIONAL,
    APRENDIZAGEM_INDUSTRIAL,
    APERFEICOAMENTO,
    TECNICO,
    ESPECIALIZACAO_TECNICA,
    GRADUACAO_TECNOLOGICA,
    POS_GRADUACAO
}
