package com.sagsa.api.models.enums;

// Método de avaliação do instrumento (sapz_instrumentos_avaliacao.tipo_metodo): GRADUAL (níveis/rubricas) ou DICOTOMICO (atingiu / não atingiu)
// Persistido como texto (EnumType.STRING): os valores gravados no banco devem ser exatamente estes.
public enum TipoMetodoAvaliacaoEnum {

    GRADUAL,
    DICOTOMICO
}
