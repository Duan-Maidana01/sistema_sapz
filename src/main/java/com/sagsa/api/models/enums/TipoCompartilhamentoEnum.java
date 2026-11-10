package com.sagsa.api.models.enums;

// Alvo do compartilhamento de uma SA (sapz_compartilhamentos.tipo_compartilhamento)
// Persistido como texto (EnumType.STRING): os valores gravados no banco devem ser exatamente estes.
public enum TipoCompartilhamentoEnum {

    GLOBAL,
    ESCOLA,
    CURSO,
    USUARIO
}
