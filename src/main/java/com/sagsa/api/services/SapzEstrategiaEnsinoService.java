package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.SapzEstrategiaEnsino;
import com.sagsa.api.models.dtos.SapzEstrategiaEnsinoRequestDTO;
import com.sagsa.api.repositories.SapzEstrategiaEnsinoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de estratégias de ensino da SA. Leitura: quem pode visualizar a SA. Escrita: autor da SA ou ADMIN.
@Service
public class SapzEstrategiaEnsinoService {

    @Autowired
    private SapzEstrategiaEnsinoRepository sapzEstrategiaEnsinoRepository;

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @Transactional(readOnly = true)
    public SapzEstrategiaEnsino findById(Long id) {
        Optional<SapzEstrategiaEnsino> sapzEstrategiaEnsino = this.sapzEstrategiaEnsinoRepository.findById(id);
        SapzEstrategiaEnsino obj = sapzEstrategiaEnsino.orElseThrow(() -> new ObjectNotFoundException(
                "Estratégia de ensino não encontrada! Id: " + id + ", Tipo: " + SapzEstrategiaEnsino.class.getName()));
        this.situacaoAprendizagemService.checkCanView(obj.getSituacaoAprendizagem());
        return obj;
    }

    @Transactional(readOnly = true)
    public List<SapzEstrategiaEnsino> findBySituacaoAprendizagem(Long situacaoAprendizagemId) {
        this.situacaoAprendizagemService.findById(situacaoAprendizagemId); // valida existência e permissão de visualização
        return this.sapzEstrategiaEnsinoRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(situacaoAprendizagemId);
    }

    @Transactional
    public SapzEstrategiaEnsino create(SapzEstrategiaEnsino obj) {
        this.situacaoAprendizagemService.checkOwner(obj.getSituacaoAprendizagem());
        obj.setId(null);
        return this.sapzEstrategiaEnsinoRepository.save(obj);
    }

    @Transactional
    public SapzEstrategiaEnsino update(SapzEstrategiaEnsino obj) {
        SapzEstrategiaEnsino newObj = findById(obj.getId());
        this.situacaoAprendizagemService.checkOwner(newObj.getSituacaoAprendizagem());
        obj.setSituacaoAprendizagem(newObj.getSituacaoAprendizagem()); // vínculo com o pai não pode ser alterado
        newObj.setNomeEstrategia(obj.getNomeEstrategia());
        newObj.setDescricaoAtividade(obj.getDescricaoAtividade());
        return this.sapzEstrategiaEnsinoRepository.save(newObj);
    }

    public void delete(Long id) {
        SapzEstrategiaEnsino obj = findById(id);
        this.situacaoAprendizagemService.checkOwner(obj.getSituacaoAprendizagem());
        try {
            this.sapzEstrategiaEnsinoRepository.deleteById(id);
            this.sapzEstrategiaEnsinoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public SapzEstrategiaEnsino fromDTO(@Valid SapzEstrategiaEnsinoRequestDTO obj) {
        SapzEstrategiaEnsino sapzEstrategiaEnsino = new SapzEstrategiaEnsino();
        sapzEstrategiaEnsino.setSituacaoAprendizagem(this.situacaoAprendizagemService.findById(obj.getSituacaoAprendizagemId()));
        sapzEstrategiaEnsino.setNomeEstrategia(obj.getNomeEstrategia());
        sapzEstrategiaEnsino.setDescricaoAtividade(obj.getDescricaoAtividade());
        return sapzEstrategiaEnsino;
    }
}
