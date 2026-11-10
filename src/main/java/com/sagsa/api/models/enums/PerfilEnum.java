package com.sagsa.api.models.enums;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Perfis de acesso do SAGSA. O "nome" do perfil na tabela "perfis" deve ser igual ao nome da constante.
@AllArgsConstructor
@Getter
public enum PerfilEnum {

    ADMIN(1, "ROLE_ADMIN", "Administrador do sistema"),
    COORDENADOR(2, "ROLE_COORDENADOR", "Coordenador pedagógico / de curso"),
    INSTRUTOR(3, "ROLE_INSTRUTOR", "Instrutor / docente");

    private Integer code;
    private String role;
    private String descricao;

    // Converte o código numérico no enum
    public static PerfilEnum toEnum(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (PerfilEnum x : PerfilEnum.values()) {
            if (code.equals(x.getCode())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Código de perfil inválido: " + code);
    }

    // Converte o nome gravado em perfis.nome no enum (sem diferenciar maiúsculas/minúsculas)
    public static PerfilEnum fromNome(String nome) {
        if (Objects.isNull(nome)) {
            return null;
        }
        for (PerfilEnum x : PerfilEnum.values()) {
            if (x.name().equalsIgnoreCase(nome.trim())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Perfil inválido: " + nome);
    }
}
