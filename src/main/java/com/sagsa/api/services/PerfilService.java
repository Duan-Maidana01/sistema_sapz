package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Perfil;
import com.sagsa.api.models.dtos.PerfilRequestDTO;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.repositories.PerfilRepository;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Transactional(readOnly = true)
    public Perfil findById(Long id) {
        Optional<Perfil> perfil = this.perfilRepository.findById(id);
        return perfil.orElseThrow(() -> new ObjectNotFoundException(
                "Perfil não encontrado! Id: " + id + ", Tipo: " + Perfil.class.getName()));
    }

    @Transactional(readOnly = true)
    public Perfil findByNome(String nome) {
        return this.perfilRepository.findByNome(nome).orElseThrow(() -> new ObjectNotFoundException(
                "Perfil não encontrado! Nome: " + nome + ", Tipo: " + Perfil.class.getName()));
    }

    @Transactional(readOnly = true)
    public List<Perfil> findAll() {
        return this.perfilRepository.findAll(Sort.by("id"));
    }

    @Transactional
    public Perfil create(Perfil obj) {
        UsuarioService.checkAdmin();
        validate(obj);
        obj.setId(null);
        return this.perfilRepository.save(obj);
    }

    @Transactional
    public Perfil update(Perfil obj) {
        UsuarioService.checkAdmin();
        Perfil newObj = findById(obj.getId());
        validate(obj);
        newObj.setNome(obj.getNome());
        newObj.setDescricao(obj.getDescricao());
        return this.perfilRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkAdmin();
        findById(id);
        try {
            this.perfilRepository.deleteById(id);
            this.perfilRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir o perfil pois há usuários vinculados!");
        }
    }

    // O nome do perfil precisa existir no PerfilEnum (é ele que vira a ROLE do Spring Security)
    private void validate(Perfil obj) {
        try {
            obj.setNome(PerfilEnum.fromNome(obj.getNome()).name());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Nome de perfil inválido. Use: ADMIN, COORDENADOR ou INSTRUTOR.");
        }
        boolean duplicado = (obj.getId() == null)
                ? this.perfilRepository.existsByNome(obj.getNome())
                : this.perfilRepository.existsByNomeAndIdNot(obj.getNome(), obj.getId());
        if (duplicado) {
            throw new DataBindingViolationException("Já existe um perfil com o nome " + obj.getNome() + ".");
        }
    }

    public Perfil fromDTO(@Valid PerfilRequestDTO obj) {
        Perfil perfil = new Perfil();
        perfil.setNome(obj.getNome());
        perfil.setDescricao(obj.getDescricao());
        return perfil;
    }
}
