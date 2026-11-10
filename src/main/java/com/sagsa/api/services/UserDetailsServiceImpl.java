package com.sagsa.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sagsa.api.models.Usuario;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.repositories.UsuarioRepository;
import com.sagsa.api.security.UserSpringSecurity;

// Busca o usuário na tabela "usuarios" pelo e-mail para o Spring Security
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = this.usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        PerfilEnum perfil;
        try {
            perfil = PerfilEnum.fromNome(usuario.getPerfil().getNome());
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Perfil do usuário não reconhecido: " + usuario.getPerfil().getNome());
        }

        return new UserSpringSecurity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenhaHash(),
                Boolean.TRUE.equals(usuario.getAtivo()),
                perfil
        );
    }
}
