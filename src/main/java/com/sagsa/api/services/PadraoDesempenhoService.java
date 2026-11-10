package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.PadraoDesempenho;
import com.sagsa.api.models.dtos.PadraoDesempenhoRequestDTO;
import com.sagsa.api.repositories.PadraoDesempenhoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de padrões de desempenho. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class PadraoDesempenhoService {

    @Autowired
    private PadraoDesempenhoRepository padraoDesempenhoRepository;

    @Autowired
    private SubfuncaoService subfuncaoService;

    @Transactional(readOnly = true)
    public PadraoDesempenho findById(Long id) {
        Optional<PadraoDesempenho> padraoDesempenho = this.padraoDesempenhoRepository.findById(id);
        PadraoDesempenho obj = padraoDesempenho.orElseThrow(() -> new ObjectNotFoundException(
                "Padrão de desempenho não encontrado! Id: " + id + ", Tipo: " + PadraoDesempenho.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<PadraoDesempenho> findAll() {
        return this.padraoDesempenhoRepository.findAll(Sort.by("id"));
    }

    @Transactional(readOnly = true)
    public List<PadraoDesempenho> findBySubfuncao(Long subfuncaoId) {
        return this.padraoDesempenhoRepository.findBySubfuncao_IdOrderByIdAsc(subfuncaoId);
    }

    @Transactional
    public PadraoDesempenho create(PadraoDesempenho obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        return this.padraoDesempenhoRepository.save(obj);
    }

    @Transactional
    public PadraoDesempenho update(PadraoDesempenho obj) {
        UsuarioService.checkGestao();
        PadraoDesempenho newObj = findById(obj.getId());
        newObj.setSubfuncao(obj.getSubfuncao());
        newObj.setDescricao(obj.getDescricao());
        return this.padraoDesempenhoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.padraoDesempenhoRepository.deleteById(id);
            this.padraoDesempenhoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public PadraoDesempenho fromDTO(@Valid PadraoDesempenhoRequestDTO obj) {
        PadraoDesempenho padraoDesempenho = new PadraoDesempenho();
        padraoDesempenho.setSubfuncao(this.subfuncaoService.findById(obj.getSubfuncaoId()));
        padraoDesempenho.setDescricao(obj.getDescricao());
        return padraoDesempenho;
    }
}
