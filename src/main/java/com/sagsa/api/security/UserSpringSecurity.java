package com.sagsa.api.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sagsa.api.models.enums.PerfilEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

// Representa o usuário autenticado (tabela "usuarios") dentro do Spring Security
@NoArgsConstructor
@Getter
public class UserSpringSecurity implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String username; // e-mail do usuário
    private String password; // senha_hash (BCrypt)
    private boolean ativo;
    private PerfilEnum perfil;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSpringSecurity(Long id, String nome, String username, String password, boolean ativo, PerfilEnum perfil) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.password = password;
        this.ativo = ativo;
        this.perfil = perfil;
        this.authorities = Set.of(new SimpleGrantedAuthority(perfil.getRole()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Usuários com ativo = false não conseguem fazer login
    @Override
    public boolean isEnabled() {
        return this.ativo;
    }

    // Verifica se o usuário possui o perfil informado
    public boolean hasRole(PerfilEnum perfil) {
        return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getRole()));
    }

    // Verifica se o usuário possui algum dos perfis informados
    public boolean hasAnyRole(PerfilEnum... perfis) {
        for (PerfilEnum p : perfis) {
            if (hasRole(p)) {
                return true;
            }
        }
        return false;
    }
}
