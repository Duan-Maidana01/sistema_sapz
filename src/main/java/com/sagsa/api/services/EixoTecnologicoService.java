package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.EixoTecnologico;
import com.sagsa.api.models.dtos.EixoTecnologicoRequestDTO;
import com.sagsa.api.repositories.EixoTecnologicoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de eixos tecnológicos. Leitura: qualquer usuário autenticado. Escrita: ADMIN.
@Service
public class EixoTecnologicoService {

    @Autowired
    private EixoTecnologicoRepository eixoTecnologicoRepository;

    @Transactional(readOnly = true)
    public EixoTecnologico findById(Long id) {
        Optional<EixoTecnologico> eixoTecnologico = this.eixoTecnologicoRepository.findById(id);
        EixoTecnologico obj = eixoTecnologico.orElseThrow(() -> new ObjectNotFoundException(
                "Eixo tecnológico não encontrado! Id: " + id + ", Tipo: " + EixoTecnologico.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<EixoTecnologico> findAll() {
        return this.eixoTecnologicoRepository.findAll(Sort.by("nome"));
    }

    @Transactional
    public EixoTecnologico create(EixoTecnologico obj) {
        UsuarioService.checkAdmin();
        obj.setId(null);
        return this.eixoTecnologicoRepository.save(obj);
    }

    @Transactional
    public EixoTecnologico update(EixoTecnologico obj) {
        UsuarioService.checkAdmin();
        EixoTecnologico newObj = findById(obj.getId());
        newObj.setNome(obj.getNome());
        return this.eixoTecnologicoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkAdmin();
        findById(id);
        try {
            this.eixoTecnologicoRepository.deleteById(id);
            this.eixoTecnologicoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public EixoTecnologico fromDTO(@Valid EixoTecnologicoRequestDTO obj) {
        EixoTecnologico eixoTecnologico = new EixoTecnologico();
        eixoTecnologico.setNome(obj.getNome());
        return eixoTecnologico;
    }
}
