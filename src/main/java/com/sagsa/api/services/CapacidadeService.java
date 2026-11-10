package com.sagsa.api.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Capacidade;
import com.sagsa.api.models.PadraoDesempenho;
import com.sagsa.api.models.dtos.CapacidadeRequestDTO;
import com.sagsa.api.models.enums.TipoCapacidadeEnum;
import com.sagsa.api.repositories.CapacidadeRepository;
import com.sagsa.api.repositories.PadraoDesempenhoRepository;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de capacidades. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class CapacidadeService {

    @Autowired
    private CapacidadeRepository capacidadeRepository;

    @Autowired
    private PadraoDesempenhoRepository padraoDesempenhoRepository;

    @Autowired
    private UnidadeCurricularService unidadeCurricularService;

    @Transactional(readOnly = true)
    public Capacidade findById(Long id) {
        Optional<Capacidade> capacidade = this.capacidadeRepository.findById(id);
        return capacidade.orElseThrow(() -> new ObjectNotFoundException(
                "Capacidade não encontrada! Id: " + id + ", Tipo: " + Capacidade.class.getName()));
    }

    @Transactional(readOnly = true)
    public List<Capacidade> findAll() {
        return this.capacidadeRepository.findAll(Sort.by("id"));
    }

    @Transactional(readOnly = true)
    public List<Capacidade> findByUnidadeCurricular(Long unidadeCurricularId, TipoCapacidadeEnum tipo) {
        this.unidadeCurricularService.findById(unidadeCurricularId);
        if (Objects.nonNull(tipo)) {
            return this.capacidadeRepository.findByUnidadeCurricular_IdAndTipoOrderByIdAsc(unidadeCurricularId, tipo);
        }
        return this.capacidadeRepository.findByUnidadeCurricular_IdOrderByIdAsc(unidadeCurricularId);
    }

    @Transactional
    public Capacidade create(Capacidade obj) {
        UsuarioService.checkGestao();
        validate(obj);
        obj.setId(null);
        return this.capacidadeRepository.save(obj);
    }

    @Transactional
    public Capacidade update(Capacidade obj) {
        UsuarioService.checkGestao();
        Capacidade newObj = findById(obj.getId());
        validate(obj);
        newObj.setUnidadeCurricular(obj.getUnidadeCurricular());
        newObj.setTipo(obj.getTipo());
        newObj.setDominio(obj.getDominio());
        newObj.setDescricao(obj.getDescricao());
        newObj.getPadroesDesempenho().clear();
        newObj.getPadroesDesempenho().addAll(obj.getPadroesDesempenho());
        return this.capacidadeRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.capacidadeRepository.deleteById(id);
            this.capacidadeRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    // Os padrões de desempenho precisam pertencer ao mesmo plano de curso da UC
    private void validate(Capacidade obj) {
        Long planoDaUc = obj.getUnidadeCurricular().getModulo().getPlanoCurso().getId();
        for (PadraoDesempenho padrao : obj.getPadroesDesempenho()) {
            Long planoDoPadrao = padrao.getSubfuncao().getFuncao().getPlanoCurso().getId();
            if (!planoDaUc.equals(planoDoPadrao)) {
                throw new BusinessRuleException("O padrão de desempenho " + padrao.getId()
                        + " não pertence ao mesmo plano de curso da unidade curricular.");
            }
        }
    }

    public Capacidade fromDTO(@Valid CapacidadeRequestDTO obj) {
        Capacidade capacidade = new Capacidade();
        capacidade.setUnidadeCurricular(this.unidadeCurricularService.findById(obj.getUnidadeCurricularId()));
        capacidade.setTipo(obj.getTipo());
        capacidade.setDominio(obj.getDominio());
        capacidade.setDescricao(obj.getDescricao());
        Set<Long> ids = Objects.nonNull(obj.getPadraoDesempenhoIds()) ? obj.getPadraoDesempenhoIds() : new HashSet<>();
        List<PadraoDesempenho> padroes = this.padraoDesempenhoRepository.findAllById(ids);
        if (padroes.size() != ids.size()) {
            throw new ObjectNotFoundException("Um ou mais padrões de desempenho informados não foram encontrados.");
        }
        capacidade.setPadroesDesempenho(new HashSet<>(padroes));
        return capacidade;
    }
}
