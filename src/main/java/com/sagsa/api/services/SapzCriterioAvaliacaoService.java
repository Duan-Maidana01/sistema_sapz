package com.sagsa.api.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.SapzCriterioAvaliacao;
import com.sagsa.api.models.dtos.SapzCriterioAvaliacaoRequestDTO;
import com.sagsa.api.repositories.SapzCriterioAvaliacaoRepository;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de critérios de avaliação. Leitura: quem pode visualizar a SA. Escrita: autor da SA ou ADMIN.
@Service
public class SapzCriterioAvaliacaoService {

    @Autowired
    private SapzCriterioAvaliacaoRepository sapzCriterioAvaliacaoRepository;

    @Autowired
    private SapzInstrumentoAvaliacaoService sapzInstrumentoAvaliacaoService;

    @Autowired
    private CapacidadeService capacidadeService;

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @Transactional(readOnly = true)
    public SapzCriterioAvaliacao findById(Long id) {
        Optional<SapzCriterioAvaliacao> sapzCriterioAvaliacao = this.sapzCriterioAvaliacaoRepository.findById(id);
        SapzCriterioAvaliacao obj = sapzCriterioAvaliacao.orElseThrow(() -> new ObjectNotFoundException(
                "Critério de avaliação não encontrado! Id: " + id + ", Tipo: " + SapzCriterioAvaliacao.class.getName()));
        this.situacaoAprendizagemService.checkCanView(obj.getInstrumento().getSituacaoAprendizagem());
        return obj;
    }

    @Transactional(readOnly = true)
    public List<SapzCriterioAvaliacao> findByInstrumento(Long instrumentoId) {
        this.sapzInstrumentoAvaliacaoService.findById(instrumentoId); // valida existência e permissão de visualização
        return this.sapzCriterioAvaliacaoRepository.findByInstrumento_IdOrderByIdAsc(instrumentoId);
    }

    @Transactional
    public SapzCriterioAvaliacao create(SapzCriterioAvaliacao obj) {
        this.situacaoAprendizagemService.checkOwner(obj.getInstrumento().getSituacaoAprendizagem());
        validate(obj);
        obj.setId(null);
        if (Objects.isNull(obj.getPrioridadePeso())) {
            obj.setPrioridadePeso(new BigDecimal("1.00"));
        }
        return this.sapzCriterioAvaliacaoRepository.save(obj);
    }

    @Transactional
    public SapzCriterioAvaliacao update(SapzCriterioAvaliacao obj) {
        SapzCriterioAvaliacao newObj = findById(obj.getId());
        this.situacaoAprendizagemService.checkOwner(newObj.getInstrumento().getSituacaoAprendizagem());
        obj.setInstrumento(newObj.getInstrumento()); // vínculo com o pai não pode ser alterado
        validate(obj);
        newObj.setCapacidade(obj.getCapacidade());
        newObj.setDescricaoCriterio(obj.getDescricaoCriterio());
        if (Objects.nonNull(obj.getPrioridadePeso())) {
            newObj.setPrioridadePeso(obj.getPrioridadePeso());
        }
        return this.sapzCriterioAvaliacaoRepository.save(newObj);
    }

    public void delete(Long id) {
        SapzCriterioAvaliacao obj = findById(id);
        this.situacaoAprendizagemService.checkOwner(obj.getInstrumento().getSituacaoAprendizagem());
        try {
            this.sapzCriterioAvaliacaoRepository.deleteById(id);
            this.sapzCriterioAvaliacaoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    // A capacidade avaliada precisa estar entre as capacidades trabalhadas na SA
    private void validate(SapzCriterioAvaliacao obj) {
        boolean capacidadeNaSa = obj.getInstrumento().getSituacaoAprendizagem().getCapacidades().stream()
                .anyMatch(c -> c.getId().equals(obj.getCapacidade().getId()));
        if (!capacidadeNaSa) {
            throw new BusinessRuleException("A capacidade informada não está vinculada a esta situação de aprendizagem.");
        }
    }

    public SapzCriterioAvaliacao fromDTO(@Valid SapzCriterioAvaliacaoRequestDTO obj) {
        SapzCriterioAvaliacao sapzCriterioAvaliacao = new SapzCriterioAvaliacao();
        sapzCriterioAvaliacao.setInstrumento(this.sapzInstrumentoAvaliacaoService.findById(obj.getInstrumentoId()));
        sapzCriterioAvaliacao.setCapacidade(this.capacidadeService.findById(obj.getCapacidadeId()));
        sapzCriterioAvaliacao.setDescricaoCriterio(obj.getDescricaoCriterio());
        sapzCriterioAvaliacao.setPrioridadePeso(obj.getPrioridadePeso());
        return sapzCriterioAvaliacao;
    }
}
