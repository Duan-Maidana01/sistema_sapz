package com.sagsa.api.services; // Pacote das classes de serviço da aplicação

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.exceptions.AuthorizationException;
import com.sagsa.api.models.Usuario;
import com.sagsa.api.models.dtos.UsuarioCreateDTO;
import com.sagsa.api.models.dtos.UsuarioUpdateDTO;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.models.projections.UsuarioProjection;
import com.sagsa.api.repositories.UsuarioRepository;
import com.sagsa.api.security.UserSpringSecurity;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // ------------------------------------------------------------------
    // Utilitários de autenticação/autorização usados por todos os services
    // ------------------------------------------------------------------

    // Retorna o usuário autenticado da requisição atual (ou null)
    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    // Exige usuário autenticado
    public static UserSpringSecurity checkAuthenticated() {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Acesso negado! Usuário não autenticado.");
        }
        return userSpringSecurity;
    }

    // Exige perfil ADMIN
    public static UserSpringSecurity checkAdmin() {
        UserSpringSecurity userSpringSecurity = checkAuthenticated();
        if (!userSpringSecurity.hasRole(PerfilEnum.ADMIN)) {
            throw new AuthorizationException("Acesso negado! Apenas administradores podem realizar esta ação.");
        }
        return userSpringSecurity;
    }

    // Exige perfil de gestão (ADMIN ou COORDENADOR)
    public static UserSpringSecurity checkGestao() {
        UserSpringSecurity userSpringSecurity = checkAuthenticated();
        if (!userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR)) {
            throw new AuthorizationException("Acesso negado! Apenas administradores e coordenadores podem realizar esta ação.");
        }
        return userSpringSecurity;
    }

    // ------------------------------------------------------------------
    // CRUD
    // ------------------------------------------------------------------

    // ADMIN/COORDENADOR visualizam qualquer usuário; os demais apenas o próprio perfil
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        UserSpringSecurity userSpringSecurity = checkAuthenticated();
        if (!userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR) && !id.equals(userSpringSecurity.getId())) {
            throw new AuthorizationException("Acesso negado! Você não possui permissão para visualizar este usuário.");
        }
        return findByIdSemPermissao(id);
    }

    // Busca interna (sem checagem de permissão) usada por outros services
    @Transactional(readOnly = true)
    public Usuario findByIdSemPermissao(Long id) {
        Optional<Usuario> usuario = this.usuarioRepository.findById(id);
        return usuario.orElseThrow(() -> new ObjectNotFoundException(
                "Usuário não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));
    }

    @Transactional(readOnly = true)
    public Usuario findLogged() {
        return findByIdSemPermissao(checkAuthenticated().getId());
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        checkGestao();
        return this.usuarioRepository.findAll(Sort.by("nome"));
    }

    // Lista resumida de usuários ativos de um perfil (ex.: /usuarios/perfil/INSTRUTOR)
    @Transactional(readOnly = true)
    public List<UsuarioProjection> findByPerfil(String nomePerfil) {
        checkGestao();
        return this.usuarioRepository.findByPerfil_NomeAndAtivoTrueOrderByNomeAsc(nomePerfil.toUpperCase());
    }

    // Somente ADMIN cadastra usuários
    @Transactional
    public Usuario create(Usuario obj) {
        checkAdmin();
        if (this.usuarioRepository.existsByEmail(obj.getEmail())) {
            throw new DataBindingViolationException("Já existe um usuário cadastrado com o e-mail " + obj.getEmail() + ".");
        }
        obj.setId(null);
        obj.setSenhaHash(this.bCryptPasswordEncoder.encode(obj.getSenhaHash()));
        if (Objects.isNull(obj.getAtivo())) {
            obj.setAtivo(true);
        }
        return this.usuarioRepository.save(obj);
    }

    // ADMIN altera qualquer usuário; os demais apenas os próprios dados (sem mudar perfil/status)
    @Transactional
    public Usuario update(Usuario obj) {
        UserSpringSecurity userSpringSecurity = checkAuthenticated();
        boolean isAdmin = userSpringSecurity.hasRole(PerfilEnum.ADMIN);
        if (!isAdmin && !obj.getId().equals(userSpringSecurity.getId())) {
            throw new AuthorizationException("Acesso negado! Você só pode alterar os seus próprios dados.");
        }

        Usuario newObj = findByIdSemPermissao(obj.getId());

        if (this.usuarioRepository.existsByEmailAndIdNot(obj.getEmail(), obj.getId())) {
            throw new DataBindingViolationException("Já existe um usuário cadastrado com o e-mail " + obj.getEmail() + ".");
        }

        boolean alteraPerfil = Objects.nonNull(obj.getPerfil()) && !obj.getPerfil().getId().equals(newObj.getPerfil().getId());
        boolean alteraStatus = Objects.nonNull(obj.getAtivo()) && !obj.getAtivo().equals(newObj.getAtivo());
        if (!isAdmin && (alteraPerfil || alteraStatus)) {
            throw new AuthorizationException("Acesso negado! Apenas administradores podem alterar perfil ou status de usuários.");
        }
        if (isAdmin && obj.getId().equals(userSpringSecurity.getId()) && Boolean.FALSE.equals(obj.getAtivo())) {
            throw new BusinessRuleException("Você não pode desativar o seu próprio usuário.");
        }

        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
        if (Objects.nonNull(obj.getSenhaHash()) && !obj.getSenhaHash().isBlank()) {
            newObj.setSenhaHash(this.bCryptPasswordEncoder.encode(obj.getSenhaHash()));
        }
        if (alteraPerfil) {
            newObj.setPerfil(obj.getPerfil());
        }
        if (alteraStatus) {
            newObj.setAtivo(obj.getAtivo());
        }
        return this.usuarioRepository.save(newObj);
    }

    // Somente ADMIN exclui usuários (e não pode excluir a si mesmo)
    public void delete(Long id) {
        UserSpringSecurity userSpringSecurity = checkAdmin();
        if (id.equals(userSpringSecurity.getId())) {
            throw new BusinessRuleException("Você não pode excluir o seu próprio usuário.");
        }
        findByIdSemPermissao(id);
        try {
            this.usuarioRepository.deleteById(id);
            this.usuarioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException(
                    "Não é possível excluir pois há entidades relacionadas (ex.: situações de aprendizagem). Desative o usuário.");
        }
    }

    // ------------------------------------------------------------------
    // Conversão DTO -> Entidade
    // ------------------------------------------------------------------

    public Usuario fromDTO(@Valid UsuarioCreateDTO obj) {
        Usuario usuario = new Usuario();
        usuario.setNome(obj.getNome());
        usuario.setEmail(obj.getEmail().trim().toLowerCase());
        usuario.setSenhaHash(obj.getSenha()); // será criptografada no create()
        usuario.setPerfil(this.perfilService.findById(obj.getPerfilId()));
        usuario.setAtivo(obj.getAtivo());
        return usuario;
    }

    public Usuario fromDTO(@Valid UsuarioUpdateDTO obj) {
        Usuario usuario = new Usuario();
        usuario.setId(obj.getId());
        usuario.setNome(obj.getNome());
        usuario.setEmail(obj.getEmail().trim().toLowerCase());
        usuario.setSenhaHash(obj.getSenha()); // será criptografada no update() se informada
        if (Objects.nonNull(obj.getPerfilId())) {
            usuario.setPerfil(this.perfilService.findById(obj.getPerfilId()));
        }
        usuario.setAtivo(obj.getAtivo());
        return usuario;
    }
}
