package com.sagsa.api.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.SapzInstrumentoAvaliacao;
import com.sagsa.api.models.dtos.SapzInstrumentoAvaliacaoRequestDTO;
import com.sagsa.api.models.enums.TipoMetodoAvaliacaoEnum;
import com.sagsa.api.repositories.SapzInstrumentoAvaliacaoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de instrumentos de avaliação da SA. Leitura: quem pode visualizar a SA. Escrita: autor da SA ou ADMIN.
@Service
public class SapzInstrumentoAvaliacaoService {

    @Autowired
    private SapzInstrumentoAvaliacaoRepository sapzInstrumentoAvaliacaoRepository;

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @Transactional(readOnly = true)
    public SapzInstrumentoAvaliacao findById(Long id) {
        Optional<SapzInstrumentoAvaliacao> sapzInstrumentoAvaliacao = this.sapzInstrumentoAvaliacaoRepository.findById(id);
        SapzInstrumentoAvaliacao obj = sapzInstrumentoAvaliacao.orElseThrow(() -> new ObjectNotFoundException(
                "Instrumento de avaliação não encontrado! Id: " + id + ", Tipo: " + SapzInstrumentoAvaliacao.class.getName()));
        this.situacaoAprendizagemService.checkCanView(obj.getSituacaoAprendizagem());
        return obj;
    }

    @Transactional(readOnly = true)
    public List<SapzInstrumentoAvaliacao> findBySituacaoAprendizagem(Long situacaoAprendizagemId) {
        this.situacaoAprendizagemService.findById(situacaoAprendizagemId); // valida existência e permissão de visualização
        return this.sapzInstrumentoAvaliacaoRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(situacaoAprendizagemId);
    }

    @Transactional
    public SapzInstrumentoAvaliacao create(SapzInstrumentoAvaliacao obj) {
        this.situacaoAprendizagemService.checkOwner(obj.getSituacaoAprendizagem());
        obj.setId(null);
        if (Objects.isNull(obj.getTipoMetodo())) {
            obj.setTipoMetodo(TipoMetodoAvaliacaoEnum.GRADUAL);
        }
        return this.sapzInstrumentoAvaliacaoRepository.save(obj);
    }

    @Transactional
    public SapzInstrumentoAvaliacao update(SapzInstrumentoAvaliacao obj) {
        SapzInstrumentoAvaliacao newObj = findById(obj.getId());
        this.situacaoAprendizagemService.checkOwner(newObj.getSituacaoAprendizagem());
        obj.setSituacaoAprendizagem(newObj.getSituacaoAprendizagem()); // vínculo com o pai não pode ser alterado
        newObj.setNomeInstrumento(obj.getNomeInstrumento());
        newObj.setDescricao(obj.getDescricao());
        if (Objects.nonNull(obj.getTipoMetodo())) {
            newObj.setTipoMetodo(obj.getTipoMetodo());
        }
        return this.sapzInstrumentoAvaliacaoRepository.save(newObj);
    }

    public void delete(Long id) {
        SapzInstrumentoAvaliacao obj = findById(id);
        this.situacaoAprendizagemService.checkOwner(obj.getSituacaoAprendizagem());
        try {
            this.sapzInstrumentoAvaliacaoRepository.deleteById(id);
            this.sapzInstrumentoAvaliacaoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public SapzInstrumentoAvaliacao fromDTO(@Valid SapzInstrumentoAvaliacaoRequestDTO obj) {
        SapzInstrumentoAvaliacao sapzInstrumentoAvaliacao = new SapzInstrumentoAvaliacao();
        sapzInstrumentoAvaliacao.setSituacaoAprendizagem(this.situacaoAprendizagemService.findById(obj.getSituacaoAprendizagemId()));
        sapzInstrumentoAvaliacao.setNomeInstrumento(obj.getNomeInstrumento());
        sapzInstrumentoAvaliacao.setDescricao(obj.getDescricao());
        sapzInstrumentoAvaliacao.setTipoMetodo(obj.getTipoMetodo());
        return sapzInstrumentoAvaliacao;
    }
}
