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

import com.sagsa.api.models.SapzCriterioRubrica;
import com.sagsa.api.models.dtos.SapzCriterioRubricaRequestDTO;
import com.sagsa.api.models.enums.TipoMetodoAvaliacaoEnum;
import com.sagsa.api.repositories.SapzCriterioRubricaRepository;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de rubricas (níveis) dos critérios. Leitura: quem pode visualizar a SA. Escrita: autor da SA ou ADMIN.
@Service
public class SapzCriterioRubricaService {

    @Autowired
    private SapzCriterioRubricaRepository sapzCriterioRubricaRepository;

    @Autowired
    private SapzCriterioAvaliacaoService sapzCriterioAvaliacaoService;

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @Transactional(readOnly = true)
    public SapzCriterioRubrica findById(Long id) {
        Optional<SapzCriterioRubrica> sapzCriterioRubrica = this.sapzCriterioRubricaRepository.findById(id);
        SapzCriterioRubrica obj = sapzCriterioRubrica.orElseThrow(() -> new ObjectNotFoundException(
                "Rubrica não encontrada! Id: " + id + ", Tipo: " + SapzCriterioRubrica.class.getName()));
        this.situacaoAprendizagemService.checkCanView(obj.getCriterio().getInstrumento().getSituacaoAprendizagem());
        return obj;
    }

    @Transactional(readOnly = true)
    public List<SapzCriterioRubrica> findByCriterio(Long criterioId) {
        this.sapzCriterioAvaliacaoService.findById(criterioId); // valida existência e permissão de visualização
        return this.sapzCriterioRubricaRepository.findByCriterio_IdOrderByNivelAsc(criterioId);
    }

    @Transactional
    public SapzCriterioRubrica create(SapzCriterioRubrica obj) {
        this.situacaoAprendizagemService.checkOwner(obj.getCriterio().getInstrumento().getSituacaoAprendizagem());
        validate(obj);
        obj.setId(null);
        if (Objects.isNull(obj.getPontuacao())) {
            obj.setPontuacao(new BigDecimal("0.00"));
        }
        return this.sapzCriterioRubricaRepository.save(obj);
    }

    @Transactional
    public SapzCriterioRubrica update(SapzCriterioRubrica obj) {
        SapzCriterioRubrica newObj = findById(obj.getId());
        this.situacaoAprendizagemService.checkOwner(newObj.getCriterio().getInstrumento().getSituacaoAprendizagem());
        obj.setCriterio(newObj.getCriterio()); // vínculo com o pai não pode ser alterado
        validate(obj);
        newObj.setNivel(obj.getNivel());
        newObj.setDescricaoRubrica(obj.getDescricaoRubrica());
        if (Objects.nonNull(obj.getPontuacao())) {
            newObj.setPontuacao(obj.getPontuacao());
        }
        return this.sapzCriterioRubricaRepository.save(newObj);
    }

    public void delete(Long id) {
        SapzCriterioRubrica obj = findById(id);
        this.situacaoAprendizagemService.checkOwner(obj.getCriterio().getInstrumento().getSituacaoAprendizagem());
        try {
            this.sapzCriterioRubricaRepository.deleteById(id);
            this.sapzCriterioRubricaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    // Cada nível só pode existir uma vez por critério; instrumentos DICOTOMICOS aceitam apenas 2 níveis
    private void validate(SapzCriterioRubrica obj) {
        Long criterioId = obj.getCriterio().getId();
        boolean duplicado = (obj.getId() == null)
                ? this.sapzCriterioRubricaRepository.existsByCriterio_IdAndNivel(criterioId, obj.getNivel())
                : this.sapzCriterioRubricaRepository.existsByCriterio_IdAndNivelAndIdNot(criterioId, obj.getNivel(), obj.getId());
        if (duplicado) {
            throw new DataBindingViolationException("Já existe uma rubrica com o nível " + obj.getNivel() + " para este critério.");
        }
        boolean dicotomico = TipoMetodoAvaliacaoEnum.DICOTOMICO.equals(obj.getCriterio().getInstrumento().getTipoMetodo());
        if (dicotomico && obj.getNivel() > 2) {
            throw new BusinessRuleException("Instrumentos dicotômicos aceitam apenas os níveis 1 (não atingiu) e 2 (atingiu).");
        }
    }

    public SapzCriterioRubrica fromDTO(@Valid SapzCriterioRubricaRequestDTO obj) {
        SapzCriterioRubrica sapzCriterioRubrica = new SapzCriterioRubrica();
        sapzCriterioRubrica.setCriterio(this.sapzCriterioAvaliacaoService.findById(obj.getCriterioId()));
        sapzCriterioRubrica.setNivel(obj.getNivel());
        sapzCriterioRubrica.setDescricaoRubrica(obj.getDescricaoRubrica());
        sapzCriterioRubrica.setPontuacao(obj.getPontuacao());
        return sapzCriterioRubrica;
    }
}
