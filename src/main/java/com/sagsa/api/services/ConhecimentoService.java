package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Conhecimento;
import com.sagsa.api.models.dtos.ConhecimentoRequestDTO;
import com.sagsa.api.repositories.ConhecimentoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de conhecimentos. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class ConhecimentoService {

    @Autowired
    private ConhecimentoRepository conhecimentoRepository;

    @Autowired
    private UnidadeCurricularService unidadeCurricularService;

    @Transactional(readOnly = true)
    public Conhecimento findById(Long id) {
        Optional<Conhecimento> conhecimento = this.conhecimentoRepository.findById(id);
        Conhecimento obj = conhecimento.orElseThrow(() -> new ObjectNotFoundException(
                "Conhecimento não encontrado! Id: " + id + ", Tipo: " + Conhecimento.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<Conhecimento> findAll() {
        return this.conhecimentoRepository.findAll(Sort.by("id"));
    }

    @Transactional(readOnly = true)
    public List<Conhecimento> findByUnidadeCurricular(Long unidadeCurricularId) {
        return this.conhecimentoRepository.findByUnidadeCurricular_IdOrderByIdAsc(unidadeCurricularId);
    }

    @Transactional
    public Conhecimento create(Conhecimento obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        return this.conhecimentoRepository.save(obj);
    }

    @Transactional
    public Conhecimento update(Conhecimento obj) {
        UsuarioService.checkGestao();
        Conhecimento newObj = findById(obj.getId());
        newObj.setUnidadeCurricular(obj.getUnidadeCurricular());
        newObj.setTopico(obj.getTopico());
        newObj.setDetalhamento(obj.getDetalhamento());
        return this.conhecimentoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.conhecimentoRepository.deleteById(id);
            this.conhecimentoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Conhecimento fromDTO(@Valid ConhecimentoRequestDTO obj) {
        Conhecimento conhecimento = new Conhecimento();
        conhecimento.setUnidadeCurricular(this.unidadeCurricularService.findById(obj.getUnidadeCurricularId()));
        conhecimento.setTopico(obj.getTopico());
        conhecimento.setDetalhamento(obj.getDetalhamento());
        return conhecimento;
    }
}
