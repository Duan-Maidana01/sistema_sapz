package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.SapzRecursoAmbiente;
import com.sagsa.api.models.dtos.SapzRecursoAmbienteRequestDTO;
import com.sagsa.api.repositories.SapzRecursoAmbienteRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de recursos e ambientes pedagógicos da SA. Leitura: quem pode visualizar a SA. Escrita: autor da SA ou ADMIN.
@Service
public class SapzRecursoAmbienteService {

    @Autowired
    private SapzRecursoAmbienteRepository sapzRecursoAmbienteRepository;

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @Transactional(readOnly = true)
    public SapzRecursoAmbiente findById(Long id) {
        Optional<SapzRecursoAmbiente> sapzRecursoAmbiente = this.sapzRecursoAmbienteRepository.findById(id);
        SapzRecursoAmbiente obj = sapzRecursoAmbiente.orElseThrow(() -> new ObjectNotFoundException(
                "Recurso/ambiente não encontrado! Id: " + id + ", Tipo: " + SapzRecursoAmbiente.class.getName()));
        this.situacaoAprendizagemService.checkCanView(obj.getSituacaoAprendizagem());
        return obj;
    }

    @Transactional(readOnly = true)
    public List<SapzRecursoAmbiente> findBySituacaoAprendizagem(Long situacaoAprendizagemId) {
        this.situacaoAprendizagemService.findById(situacaoAprendizagemId); // valida existência e permissão de visualização
        return this.sapzRecursoAmbienteRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(situacaoAprendizagemId);
    }

    @Transactional
    public SapzRecursoAmbiente create(SapzRecursoAmbiente obj) {
        this.situacaoAprendizagemService.checkOwner(obj.getSituacaoAprendizagem());
        obj.setId(null);
        return this.sapzRecursoAmbienteRepository.save(obj);
    }

    @Transactional
    public SapzRecursoAmbiente update(SapzRecursoAmbiente obj) {
        SapzRecursoAmbiente newObj = findById(obj.getId());
        this.situacaoAprendizagemService.checkOwner(newObj.getSituacaoAprendizagem());
        obj.setSituacaoAprendizagem(newObj.getSituacaoAprendizagem()); // vínculo com o pai não pode ser alterado
        newObj.setAmbientePedagogico(obj.getAmbientePedagogico());
        newObj.setRecursosDidaticos(obj.getRecursosDidaticos());
        return this.sapzRecursoAmbienteRepository.save(newObj);
    }

    public void delete(Long id) {
        SapzRecursoAmbiente obj = findById(id);
        this.situacaoAprendizagemService.checkOwner(obj.getSituacaoAprendizagem());
        try {
            this.sapzRecursoAmbienteRepository.deleteById(id);
            this.sapzRecursoAmbienteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public SapzRecursoAmbiente fromDTO(@Valid SapzRecursoAmbienteRequestDTO obj) {
        SapzRecursoAmbiente sapzRecursoAmbiente = new SapzRecursoAmbiente();
        sapzRecursoAmbiente.setSituacaoAprendizagem(this.situacaoAprendizagemService.findById(obj.getSituacaoAprendizagemId()));
        sapzRecursoAmbiente.setAmbientePedagogico(obj.getAmbientePedagogico());
        sapzRecursoAmbiente.setRecursosDidaticos(obj.getRecursosDidaticos());
        return sapzRecursoAmbiente;
    }
}
